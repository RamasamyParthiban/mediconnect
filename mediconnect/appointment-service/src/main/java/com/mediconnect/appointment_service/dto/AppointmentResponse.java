package com.mediconnect.appointment_service.dto;

import com.mediconnect.appointment_service.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long slotId;

    private LocalDateTime booksAt;

    private String notes;

    private AppointmentStatus appointmentStatus;
}
