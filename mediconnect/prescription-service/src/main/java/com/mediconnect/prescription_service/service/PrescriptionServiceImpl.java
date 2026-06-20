package com.mediconnect.prescription_service.service;

import com.mediconnect.prescription_service.client.AppointmentClient;
import com.mediconnect.prescription_service.client.DoctorClient;
import com.mediconnect.prescription_service.client.UserClient;
import com.mediconnect.prescription_service.dto.*;
import com.mediconnect.prescription_service.event.PrescriptionEvent;
import com.mediconnect.prescription_service.model.Medicine;
import com.mediconnect.prescription_service.model.Prescription;
import com.mediconnect.prescription_service.publisher.MessagePublisher;
import com.mediconnect.prescription_service.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    AppointmentClient appointmentClient;

    @Autowired
    MessagePublisher messagePublisher;

    @Autowired
    DoctorClient doctorClient;

    @Autowired
    UserClient userClient;

    @Override
    public PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest) {

        System.out.println("Current UserID "+getCurrentUserId());

        System.out.println();

        DoctorResponse doctorResponse = doctorClient.getDoctorByUserId(getCurrentUserId());

        if (doctorResponse == null) {
            throw new RuntimeException("Doctor Not Found");
        }

        AppointmentResponse appointment = appointmentClient.getAppointmentById(prescriptionRequest.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment Not Found");
        }

        if (prescriptionRepository.findByAppointmentId(prescriptionRequest.getAppointmentId()).isPresent()) {
            throw new RuntimeException("Prescription already exists for this appointment!");
        }

        Prescription prescription = Prescription
                .builder()
                .appointmentId(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(doctorResponse.getId())
                .instructions(prescriptionRequest.getInstructions())
                .prescribedAt(LocalDateTime.now())
                .build();

        List<Medicine> medicines = prescriptionRequest.getMedicines().stream().map(m -> {
            Medicine medicine = mapMedicineToRequest(m);
            medicine.setPrescription(prescription);
            return medicine;
        }).collect(Collectors.toList());

        prescription.setMedicines(medicines);

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        try{

            DoctorResponse doctor = doctorClient.getDoctorById(savedPrescription.getDoctorId());

            UserResponse patient = userClient.getUserById(savedPrescription.getPatientId());

            PrescriptionEvent prescriptionEvent = PrescriptionEvent
                    .builder()
                    .prescriptionId(savedPrescription.getId())
                    .patientId(savedPrescription.getPatientId())
                    .patientEmail(patient.getEmail())
                    .doctorName(doctor.getName())
                    .instructions(savedPrescription.getInstructions())
                    .build();

            messagePublisher.publishPrescriptionEvent(prescriptionEvent, "prescription.created");

        }catch (Exception e){
            System.out.println("Notification failed: " + e.getMessage());
        }

        return mapToRespond(savedPrescription);
    }

    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {

        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Prescription Not Found"));

        return mapToRespond(prescription);
    }

    @Override
    public List<PrescriptionResponse> getAllPrescriptionsForPatient() {
        return prescriptionRepository.findByPatientId(getCurrentUserId()).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getAllPrescriptionsForDoctor() {
        return prescriptionRepository.findByDoctorId(getCurrentUserId()).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    private Long getCurrentUserId() {

        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName().split(":")[1]);
    }

    @Override
    public PrescriptionResponse getPrescriptionByAppointmentId(Long appointmentId) {

        Prescription prescription = prescriptionRepository.findByAppointmentId(appointmentId).orElseThrow(() -> new RuntimeException("Prescription Not Found"));

        return mapToRespond(prescription);
    }

    @Override
    @Transactional
    public PrescriptionResponse updatePrescription(Long prescriptionId, PrescriptionRequest prescriptionRequest) {

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new RuntimeException("Prescription Not Found"));

        if (!prescription.getDoctorId().equals(getCurrentUserId())) {
            throw new RuntimeException("Unauthorized — not your prescription!");
        }

        prescription.getMedicines().clear();

        List<Medicine> updatedMedicines = prescriptionRequest.getMedicines()
                .stream()
                .map(m -> {
                    Medicine medicine = mapMedicineToRequest(m);
                    medicine.setPrescription(prescription);
                    return medicine;
                })
                .collect(Collectors.toList());

        prescription.getMedicines().addAll(updatedMedicines);
        prescription.setInstructions(prescriptionRequest.getInstructions());

        // ← explicitly save to get generated IDs back!
        Prescription saved = prescriptionRepository.save(prescription);

        return mapToRespond(saved);
    }

    private Medicine mapMedicineToRequest(MedicineRequest medicineRequest) {

        return Medicine
                .builder()
                .name(medicineRequest.getName())
                .dosage(medicineRequest.getDosage())
                .frequency(medicineRequest.getFrequency())
                .duration(medicineRequest.getDuration())
                .build();
    }

    private PrescriptionResponse mapToRespond(Prescription prescription) {

        return PrescriptionResponse
                .builder()
                .id(prescription.getId())
                .appointmentId(prescription.getAppointmentId())
                .doctorId(prescription.getDoctorId())
                .patientId(prescription.getPatientId())
                .prescribedAt(prescription.getPrescribedAt())
                .instructions(prescription.getInstructions())
                .medicines(prescription.getMedicines().stream().map(this::mapMedicineToRespond).collect(Collectors.toList()))
                .build();
    }


    private MedicineResponse mapMedicineToRespond(Medicine medicine) {

        return MedicineResponse
                .builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .dosage(medicine.getDosage())
                .frequency(medicine.getFrequency())
                .duration(medicine.getDuration())
                .build();
    }

}
