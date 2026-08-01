package com.mediconnect.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequest {

    private String name;

    //Un-Used
    //private String email;

    private Long phone;

    private String specialization;

    private Integer experience;

    private String bio;

    private Double consultationFee;

    private String location;

}
