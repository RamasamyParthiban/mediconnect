package com.mediconnect.payment_service.client;

import com.mediconnect.payment_service.config.FeignClientConfig;
import com.mediconnect.payment_service.dto.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "APPOINTMENT-SERVICE", configuration = FeignClientConfig.class)
public interface AppointmentClient {

    @GetMapping("/api/appointments/{id}")
    AppointmentResponse getAppointmentById(@PathVariable Long id);

}
