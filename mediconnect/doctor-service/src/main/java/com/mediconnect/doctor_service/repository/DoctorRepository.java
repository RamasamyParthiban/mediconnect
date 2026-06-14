package com.mediconnect.doctor_service.repository;

import com.mediconnect.doctor_service.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor , Long> {

    Optional<Doctor> findByUserId(Long userId);

    List<Doctor> findBySpecialization(String specialization);
}
