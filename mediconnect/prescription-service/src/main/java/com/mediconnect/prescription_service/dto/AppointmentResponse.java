package com.mediconnect.prescription_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long slotId;
}
