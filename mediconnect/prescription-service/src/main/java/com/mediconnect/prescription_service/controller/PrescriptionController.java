package com.mediconnect.prescription_service.controller;

import com.mediconnect.prescription_service.dto.PrescriptionRequest;
import com.mediconnect.prescription_service.dto.PrescriptionResponse;
import com.mediconnect.prescription_service.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<?> savePrescription(@RequestBody PrescriptionRequest prescriptionRequest) {

        try {
            PrescriptionResponse prescriptionResponse = prescriptionService.savePrescription(prescriptionRequest);
            return ResponseEntity.ok(prescriptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable Long id) {
        try {
            PrescriptionResponse prescription = prescriptionService.getPrescriptionById(id);
            return ResponseEntity.ok(prescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/patient")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptionsForPatient() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptionsForPatient());
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptionsForDoctor() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptionsForDoctor());
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPresciptionByAppointmentId(@PathVariable Long appointmentId) {
        try {
            PrescriptionResponse prescription = prescriptionService.getPrescriptionByAppointmentId(appointmentId);
            return ResponseEntity.ok(prescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{prescriptionId}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long prescriptionId, @RequestBody PrescriptionRequest prescriptionRequest){
        try {
            PrescriptionResponse prescription = prescriptionService.updatePrescription(prescriptionId, prescriptionRequest);
            return ResponseEntity.ok(prescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
