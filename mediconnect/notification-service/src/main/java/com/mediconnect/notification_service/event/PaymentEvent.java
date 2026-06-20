package com.mediconnect.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEvent {

    private Long paymentId;

    private Long patientId;

    private String patientEmail;

    private Double amount;

    private String transactionId;

    private String paymentMethod;
}
