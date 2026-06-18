package com.mediconnect.prescription_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {

    private Long id;

    private Long appointmentId;

    private Long doctorId;

    private Long patientId;

    private List<MedicineResponse> medicines;

    private String instructions;

    private LocalDateTime prescribedAt;

}
