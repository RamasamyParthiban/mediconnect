package com.mediconnect.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponse {

    private Long id;

    private String name;

    private String email;

    private Long phone;

    private Long userId;

    private String specialization;

    private Integer experience;

    private String bio;

    private Double consultationFee;

    private String location;

    private List<SlotResponse> slots;
}
