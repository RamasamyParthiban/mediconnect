package com.mediconnect.doctor_service.controller;

import com.mediconnect.doctor_service.dto.DoctorRequest;
import com.mediconnect.doctor_service.dto.DoctorResponse;
import com.mediconnect.doctor_service.dto.SlotRequest;
import com.mediconnect.doctor_service.dto.SlotResponse;
import com.mediconnect.doctor_service.serice.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody DoctorRequest doctorRequest) {

        DoctorResponse doctorResponse = doctorService.registerDoctor(doctorRequest);

        return ResponseEntity.ok(doctorResponse);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            DoctorResponse doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/userId/{userID}")
    public ResponseEntity<?> getDoctorByUserId(@PathVariable Long userID) {

        try {
            DoctorResponse doctor = doctorService.getDoctorByUserId(userID);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorRequest doctorRequest) {
        try {
            DoctorResponse doctorResponse = doctorService.updateDoctorProfile(doctorRequest);
            return ResponseEntity.ok(doctorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/slot")
    public ResponseEntity<SlotResponse> addAvailabilitySlot(@RequestBody SlotRequest slotRequest) {

        SlotResponse slotResponse = doctorService.addAvailabilitySlot(slotRequest);

        return ResponseEntity.ok(slotResponse);
    }

    @GetMapping("/slot/{slotId}")
    public ResponseEntity<?> getSlotById(@PathVariable Long slotId) {
        try {
            SlotResponse slotResponse = doctorService.getSlotById(slotId);
            return ResponseEntity.ok(slotResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/slots/{doctorID}")
    public ResponseEntity<List<SlotResponse>> getAllSlotForDoctor(@PathVariable Long doctorID) {

        List<SlotResponse> allSlotsForDoctor = doctorService.getAllSlotsForDoctor(doctorID);

        return ResponseEntity.ok(allSlotsForDoctor);
    }

    @GetMapping("/slots/available/{doctorID}")
    public ResponseEntity<List<SlotResponse>> getAvailableSlotForDoctor(@PathVariable Long doctorID) {

        List<SlotResponse> availableSlots = doctorService.getAvailableSlots(doctorID, false);

        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/{specialization}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialization(@PathVariable String specialization) {

        List<DoctorResponse> doctorsBySpecialization = doctorService.getDoctorsBySpecialization(specialization);

        return ResponseEntity.ok(doctorsBySpecialization);
    }

    @PutMapping("/slots/{slotId}/status")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Long slotId, @RequestParam boolean isBooked) {
        try {
            SlotResponse slotResponse = doctorService.updateSlotStatus(slotId, isBooked);
            return ResponseEntity.ok(slotResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        List<DoctorResponse> allDoctors = doctorService.getAllDoctors();

        return ResponseEntity.ok(allDoctors);
    }
}
