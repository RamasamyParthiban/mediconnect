package com.mediconnect.appointment_service.client;

import com.mediconnect.appointment_service.config.FeignClientConfig;
import com.mediconnect.appointment_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/users/{email}")
    UserResponse getUserByEmail(@PathVariable String email);

}
