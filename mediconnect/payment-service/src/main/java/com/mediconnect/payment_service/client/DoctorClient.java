package com.mediconnect.payment_service.client;

import com.mediconnect.payment_service.config.FeignClientConfig;
import com.mediconnect.payment_service.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="DOCTOR-SERVICE", configuration = FeignClientConfig.class)
public interface DoctorClient {

    @GetMapping("/api/doctors/id/{id}")
    DoctorResponse getDoctorById(@PathVariable Long id);

}