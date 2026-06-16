package com.mediconnect.appointment_service.service;

import com.mediconnect.appointment_service.dto.AppointmentRequest;
import com.mediconnect.appointment_service.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse cancelAppointment(Long appointmentId);

    AppointmentResponse getAppointmentById(Long id);

    List<AppointmentResponse> getPatientAppointments();

    List<AppointmentResponse> getDoctorAppointments();

}

