package com.mediconnect.appointment_service.service;

import com.mediconnect.appointment_service.client.DoctorClient;
import com.mediconnect.appointment_service.client.UserClient;
import com.mediconnect.appointment_service.dto.*;
import com.mediconnect.appointment_service.event.AppointmentEvent;
import com.mediconnect.appointment_service.model.Appointment;
import com.mediconnect.appointment_service.model.AppointmentStatus;
import com.mediconnect.appointment_service.publisher.MessagePublisher;
import com.mediconnect.appointment_service.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorClient doctorClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MessagePublisher messagePublisher;

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest) {

        SlotResponse slot = doctorClient.getSlotById(appointmentRequest.getSlotId());

        if (slot.isBooked()) {
            throw new RuntimeException("Slot Already Booked");
        }

        Appointment appointment = appointmentRepository.save(Appointment
                .builder()
                .patientId(getCurrentUserId())
                .doctorId(appointmentRequest.getDoctorId())
                .slotId(appointmentRequest.getSlotId())
                .bookedAt(LocalDateTime.now())
                .notes(appointmentRequest.getNotes())
                .appointmentStatus(AppointmentStatus.PENDING)
                .build());

        doctorClient.updateSlotStatus(appointment.getSlotId(), true);

        //Publish Event to RabbitMQ

        try {

            UserResponse patient = userClient.getUserByEmail(getCurrentEmail());

            DoctorResponse doctor = doctorClient.getDoctorById(appointmentRequest.getDoctorId());

            AppointmentEvent appointmentEvent = AppointmentEvent
                    .builder()
                    .appointmentId(appointment.getId())
                    .patientId(appointment.getPatientId())
                    .doctorId(appointment.getDoctorId())
                    .patientEmail(patient.getEmail())
                    .doctorName(doctor.getName())
                    .appointmentTime(slot.getDateTime())
                    .status("BOOKED")
                    .build();

            messagePublisher.publishAppointmentEvent(appointmentEvent, "appointment.booked");

        } catch (Exception e) {
            //Don't fail appointment if notification fails
            System.out.println("Notification failed: " + e.getMessage());
        }

        return mapToRespond(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        if (appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment Already Cancelled");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        doctorClient.updateSlotStatus(appointment.getSlotId(), false);

        //Publish Event to RabbitMQ

        try {
            //Always use patient id, not getCurrentEmail()
            UserResponse patient = userClient.getUserById(appointment.getPatientId());

            DoctorResponse doctor = doctorClient.getDoctorById(appointment.getDoctorId());

            AppointmentEvent appointmentEvent = AppointmentEvent.builder()
                    .appointmentId(appointment.getId())
                    .patientId(appointment.getPatientId())
                    .doctorId(appointment.getDoctorId())
                    .patientEmail(patient.getEmail())
                    .doctorName(doctor.getName())
                    .appointmentTime(null)
                    .status("CANCELLED")
                    .build();

            messagePublisher.publishAppointmentEvent(appointmentEvent, "appointment.cancelled");

        } catch (Exception e) {
            //Don't fail appointment if notification fails
            System.out.println("Notification failed: " + e.getMessage());
        }


        return mapToRespond(appointment);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return mapToRespond(appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found")));
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments() {
        return appointmentRepository.findByPatientId(getCurrentUserId()).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments() {

        Long currentUserId = getCurrentUserId();
        try{
            DoctorResponse doctorByUserId = doctorClient.getDoctorByUserId(currentUserId);

            return appointmentRepository.findByDoctorId(doctorByUserId.getId()).stream().map(this::mapToRespond).collect(Collectors.toList());
        }catch (Exception e){
            //Doctor has no profile yet -> return empty list|
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public AppointmentResponse confirmAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        if (appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot confirm cancelled appointments!");
        }
        
        appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        Appointment confirmedAppointment = appointmentRepository.save(appointment);

        //Publish Event to RabbitMQ
        try{
            UserResponse patient = userClient.getUserById(appointment.getPatientId());

            DoctorResponse doctor = doctorClient.getDoctorById(appointment.getDoctorId());

            SlotResponse slot = doctorClient.getSlotById(appointment.getSlotId());

            AppointmentEvent event = AppointmentEvent.builder()
                    .appointmentId(confirmedAppointment.getId())
                    .patientId(confirmedAppointment.getPatientId())
                    .doctorId(confirmedAppointment.getDoctorId())
                    .patientEmail(patient.getEmail())
                    .doctorName(doctor.getName())
                    .appointmentTime(slot.getDateTime())
                    .status("CONFIRMED")
                    .build();

            messagePublisher.publishAppointmentEvent(event, "appointment.confirmed");


        }catch (Exception e){
            System.out.println("Notification failed: "+e.getMessage());
        }
        return mapToRespond(confirmedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        if (appointment.getAppointmentStatus() != AppointmentStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed appointments can be completed");
        }

        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);;

        Appointment completedAppointment = appointmentRepository.save(appointment);

        //Publish Event to RabbitMQ
        try{
            UserResponse patient = userClient.getUserById(appointment.getPatientId());

            DoctorResponse doctor = doctorClient.getDoctorById(appointment.getId());

            AppointmentEvent event = AppointmentEvent.builder()
                    .appointmentId(completedAppointment.getId())
                    .patientId(completedAppointment.getPatientId())
                    .doctorId(completedAppointment.getDoctorId())
                    .patientEmail(patient.getEmail())
                    .doctorName(doctor.getName())
                    .appointmentTime(null)
                    .status("COMPLETED")
                    .build();

            messagePublisher.publishAppointmentEvent(event, "appointment.completed");

        } catch (Exception e) {
            System.out.println("Notification failed: "+e.getMessage());
        }

        return mapToRespond(completedAppointment);
    }

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName().split(":")[1]);
    }

    private String getCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName().split(":")[0];
    }

    private AppointmentResponse mapToRespond(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .slotId(appointment.getSlotId())
                .booksAt(appointment.getBookedAt())
                .notes(appointment.getNotes())
                .appointmentStatus(appointment.getAppointmentStatus())
                .build();
    }
}
