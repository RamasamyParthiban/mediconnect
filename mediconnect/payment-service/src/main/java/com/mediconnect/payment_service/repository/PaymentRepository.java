package com.mediconnect.payment_service.repository;

import com.mediconnect.payment_service.dto.PaymentResponse;
import com.mediconnect.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPatientId(Long patientId);

    Optional<Payment> findByAppointmentId(Long patientId);

    Optional<Payment> findByTransactionId(String transactionId);
}
