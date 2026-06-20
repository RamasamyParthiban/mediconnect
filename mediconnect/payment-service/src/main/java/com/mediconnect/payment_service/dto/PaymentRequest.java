package com.mediconnect.payment_service.dto;

import com.mediconnect.payment_service.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {

    private Long appointmentId;

    private PaymentMethod paymentMethod;

}
