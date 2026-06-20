package com.mediconnect.payment_service.service;

import com.mediconnect.payment_service.dto.PaymentRequest;
import com.mediconnect.payment_service.dto.PaymentResponse;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest paymentRequest);

    List<PaymentResponse> getPaymentHistory();

    PaymentResponse getPaymentByAppointmentID(Long appointmentID);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    PaymentResponse getPaymentById(Long id);
}
