package com.mediconnect.prescription_service.client;

import com.mediconnect.prescription_service.config.FeignClientConfig;
import com.mediconnect.prescription_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/users/id/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
