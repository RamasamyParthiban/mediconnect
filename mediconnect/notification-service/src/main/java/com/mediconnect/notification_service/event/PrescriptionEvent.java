package com.mediconnect.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionEvent {

    private Long prescriptionId;

    private Long patientId;

    private String patientEmail;

    private String doctorName;

    private String instructions;
}
