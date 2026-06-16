package com.mediconnect.appointment_service.controller;

import com.mediconnect.appointment_service.dto.AppointmentRequest;
import com.mediconnect.appointment_service.dto.AppointmentResponse;
import com.mediconnect.appointment_service.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        try {
            AppointmentResponse appointmentResponse = appointmentService.bookAppointment(appointmentRequest);

            return ResponseEntity.ok(appointmentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PutMapping("/cancel/{appointmentID}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentID){
        try {
            AppointmentResponse appointmentResponse = appointmentService.cancelAppointment(appointmentID);
            return ResponseEntity.ok(appointmentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id){
        try {
            AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointmentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments() {

        return ResponseEntity.ok(appointmentService.getDoctorAppointments());
    }

    @GetMapping("/patient")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments() {

        return ResponseEntity.ok(appointmentService.getPatientAppointments());
    }

}
