package com.mediconnect.doctor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private Long phone;

    @Column(nullable = false,unique = true)
    private Long userId;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "doctor" , cascade = CascadeType.ALL)
    private List<AvailabilitySlot> slots;

}
