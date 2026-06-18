package com.mediconnect.prescription_service.repository;

import com.mediconnect.prescription_service.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientId(Long patientId);

    Optional<Prescription> findByAppointmentId(Long appointmentId);

    List<Prescription> findByDoctorId(Long doctorId);

}
