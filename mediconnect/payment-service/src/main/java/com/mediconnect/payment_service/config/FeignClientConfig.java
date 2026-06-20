package com.mediconnect.payment_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Get current HTTP request
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder
                                .getRequestAttributes();

                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();

                    // Extract token from incoming request
                    String authHeader = request.getHeader("Authorization");

                    // Forward same token to outgoing FeignClient request
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        template.header("Authorization", authHeader);
                    }
                }
            }
        };
    }
}
