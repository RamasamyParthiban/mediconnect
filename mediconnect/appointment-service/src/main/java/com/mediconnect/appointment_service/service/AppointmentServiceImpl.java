package com.mediconnect.appointment_service.service;

import com.mediconnect.appointment_service.client.DoctorClient;
import com.mediconnect.appointment_service.dto.AppointmentRequest;
import com.mediconnect.appointment_service.dto.AppointmentResponse;
import com.mediconnect.appointment_service.dto.DoctorResponse;
import com.mediconnect.appointment_service.dto.SlotResponse;
import com.mediconnect.appointment_service.model.Appointment;
import com.mediconnect.appointment_service.model.AppointmentStatus;
import com.mediconnect.appointment_service.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorClient doctorClient;

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

        DoctorResponse doctorByUserId = doctorClient.getDoctorByUserId(currentUserId);

        return appointmentRepository.findByDoctorId(doctorByUserId.getId()).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName().split(":")[1]);
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
