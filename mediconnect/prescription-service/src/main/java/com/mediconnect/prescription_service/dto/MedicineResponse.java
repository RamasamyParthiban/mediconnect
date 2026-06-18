package com.mediconnect.prescription_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponse {

    private Long id;

    private String name;

    private String dosage;

    private String frequency;

    private String duration;

}
