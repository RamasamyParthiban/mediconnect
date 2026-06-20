package com.mediconnect.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentEvent {

    private Long appointmentId;

    private Long patientId;

    private Long doctorId;

    private String patientEmail;

    private String doctorName;

    private LocalDateTime appointmentTime;

    private String status;

}
