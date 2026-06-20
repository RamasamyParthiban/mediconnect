package com.mediconnect.prescription_service.client;

import com.mediconnect.prescription_service.config.FeignClientConfig;
import com.mediconnect.prescription_service.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCTOR-SERVICE", configuration = FeignClientConfig.class)
public interface DoctorClient {

    @GetMapping("/api/doctors/id/{id}")
    DoctorResponse getDoctorById(@PathVariable Long id);

    @GetMapping("/api/doctors/userId/{userId}")
    DoctorResponse getDoctorByUserId(@PathVariable Long userId);
}
