package com.mediconnect.doctor_service.repository;

import com.mediconnect.doctor_service.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctorId(Long doctorID);

    List<AvailabilitySlot> findByDoctorIdAndIsBooked(Long doctorId, boolean isBooked);

}
