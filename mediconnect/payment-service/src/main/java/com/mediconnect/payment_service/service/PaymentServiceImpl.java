package com.mediconnect.payment_service.service;

import com.mediconnect.payment_service.client.AppointmentClient;
import com.mediconnect.payment_service.client.DoctorClient;
import com.mediconnect.payment_service.dto.AppointmentResponse;
import com.mediconnect.payment_service.dto.DoctorResponse;
import com.mediconnect.payment_service.dto.PaymentRequest;
import com.mediconnect.payment_service.dto.PaymentResponse;
import com.mediconnect.payment_service.event.PaymentEvent;
import com.mediconnect.payment_service.model.Payment;
import com.mediconnect.payment_service.model.PaymentStatus;
import com.mediconnect.payment_service.publisher.MessagePublisher;
import com.mediconnect.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    DoctorClient doctorClient;

    @Autowired
    AppointmentClient appointmentClient;

    @Autowired
    MessagePublisher messagePublisher;

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest) {

        AppointmentResponse appointment = appointmentClient.getAppointmentById(paymentRequest.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment Not Found");
        }

        DoctorResponse doctor = doctorClient.getDoctorById(appointment.getDoctorId());

        if (doctor == null) {
            throw new RuntimeException("Doctor Not Found");
        }

        Payment payment = paymentRepository.save(Payment
                .builder()
                .appointmentId(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(doctor.getId())
                .amount(doctor.getConsultationFee())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId(UUID.randomUUID().toString())
                .paidAt(LocalDateTime.now())
                .build());

        // publish Event in RabbitMQ

        try {

            PaymentEvent paymentEvent = PaymentEvent.builder()
                    .paymentId(payment.getId())
                    .patientId(payment.getPatientId())
                    .patientEmail(getCurrentEmail())
                    .amount(payment.getAmount())
                    .transactionId(payment.getTransactionId())
                    .paymentMethod(String.valueOf(payment.getPaymentMethod()))
                    .build();

            messagePublisher.publishPaymentEvent(paymentEvent, "payment.success");

        }catch (Exception e){
            System.out.println("Notification failed: " + e.getMessage());
        }

        return mapToRespond(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentHistory() {

        return paymentRepository.findByPatientId(getCurrentUserId()).stream().map(this::mapToRespond).collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentByAppointmentID(Long appointmentID) {

        Payment payment = paymentRepository.findByAppointmentId(appointmentID).orElseThrow(() -> new RuntimeException("No Payment Found for this Appointment"));

        return mapToRespond(payment);
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {

        Payment payment = paymentRepository.findByTransactionId(transactionId).orElseThrow(() -> new RuntimeException("No Payment Found with the Transaction"));

        return mapToRespond(payment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("No Payment Found with the Given Id"));

        return mapToRespond(payment);
    }

    private PaymentResponse mapToRespond(Payment payment) {

        return PaymentResponse
                .builder()
                .id(payment.getId())
                .appointmentId(payment.getAppointmentId())
                .patientId(payment.getPatientId())
                .doctorId(payment.getDoctorId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paidAt(payment.getPaidAt())
                .build();
    }

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName().split(":")[1]);
    }

    private String getCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName().split(":")[0];
    }
}
