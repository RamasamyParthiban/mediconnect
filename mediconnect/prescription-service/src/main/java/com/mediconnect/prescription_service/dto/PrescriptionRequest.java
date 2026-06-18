package com.mediconnect.prescription_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionRequest {

    private Long appointmentId;

    private List<MedicineRequest> medicines;

    private String instructions;

}
