package com.mediconnect.doctor_service.serice;

import com.mediconnect.doctor_service.dto.DoctorRequest;
import com.mediconnect.doctor_service.dto.DoctorResponse;
import com.mediconnect.doctor_service.dto.SlotRequest;
import com.mediconnect.doctor_service.dto.SlotResponse;
import com.mediconnect.doctor_service.model.AvailabilitySlot;
import com.mediconnect.doctor_service.model.Doctor;
import com.mediconnect.doctor_service.repository.AvailabilitySlotRepository;
import com.mediconnect.doctor_service.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AvailabilitySlotRepository slotRepository;

    @Override
    public DoctorResponse registerDoctor(DoctorRequest doctorRequest) {

        Doctor doctor = doctorRepository
                .save(Doctor
                        .builder()
                        .name(doctorRequest.getName())
                        .email(getCurrentEmail())
                        .phone(doctorRequest.getPhone())
                        .userId(getCurrentUserID())
                        .specialization(doctorRequest.getSpecialization())
                        .experience(doctorRequest.getExperience())
                        .bio(doctorRequest.getBio())
                        .consultationFee(doctorRequest.getConsultationFee())
                        .location(doctorRequest.getLocation())
                        .build());

        return mapToRespond(doctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapToRespond(doctor);
    }

    @Override
    public DoctorResponse getDoctorByUserId(Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapToRespond(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctorProfile(DoctorRequest doctorRequest) {

        Doctor doctor = doctorRepository.findByUserId(getCurrentUserID()).orElseThrow(() -> new RuntimeException("Doctor Not Found"));

        doctor.setName(doctorRequest.getName());
        doctor.setEmail(getCurrentEmail());
        doctor.setPhone(doctorRequest.getPhone());
        doctor.setUserId(getCurrentUserID());
        doctor.setSpecialization(doctorRequest.getSpecialization());
        doctor.setExperience(doctorRequest.getExperience());
        doctor.setBio(doctorRequest.getBio());
        doctor.setConsultationFee(doctorRequest.getConsultationFee());
        doctor.setLocation(doctorRequest.getLocation());

        return mapToRespond(doctor);

    }

    @Override
    public SlotResponse addAvailabilitySlot(SlotRequest slotRequest) {

        Doctor doctor = doctorRepository.findByUserId(getCurrentUserID()).orElseThrow();

        AvailabilitySlot availabilitySlot = slotRepository.save(AvailabilitySlot.builder().datetime(slotRequest.getDatetime()).isBooked(false).doctor(doctor).build());

        return SlotResponse.builder().id(availabilitySlot.getId()).booked(availabilitySlot.isBooked()).datetime(availabilitySlot.getDatetime()).build();
    }

    @Override
    public List<SlotResponse> getAllSlotsForDoctor(Long doctorID) {

        return slotRepository.findByDoctorId(doctorID).stream().map(this::mapSlotToRespond).collect(Collectors.toList());
    }

    @Override
    public List<SlotResponse> getAvailableSlots(Long doctorID, boolean isBooked) {

        return slotRepository.findByDoctorIdAndIsBooked(doctorID, isBooked).stream().map(this::mapSlotToRespond).collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {

        return doctorRepository.findBySpecialization(specialization).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll().stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    public String getCurrentEmail() {

        return SecurityContextHolder.getContext().getAuthentication().getName().split(":")[0];
    }

    public Long getCurrentUserID() {

        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName().split(":")[1]);
    }

    public String getCurrentRole() {

        return SecurityContextHolder.getContext().getAuthentication().getName().split(":")[2];
    }

    private DoctorResponse mapToRespond(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .userId(doctor.getUserId())
                .specialization(doctor.getSpecialization())
                .experience(doctor.getExperience())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .location(doctor.getLocation())
                .slots(doctor.getSlots() != null ? doctor.getSlots().stream().map(this::mapSlotToRespond).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    private SlotResponse mapSlotToRespond(AvailabilitySlot availabilitySlot) {

        return new SlotResponse(availabilitySlot.getId(), availabilitySlot.isBooked(), availabilitySlot.getDatetime());
    }
}
