package com.mediconnect.appointment_service.client;

import com.mediconnect.appointment_service.config.FeignClientConfig;
import com.mediconnect.appointment_service.dto.DoctorResponse;
import com.mediconnect.appointment_service.dto.SlotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="DOCTOR-SERVICE", configuration = FeignClientConfig.class)
public interface DoctorClient {

    @GetMapping("/api/doctors/slot/{slotId}")
    SlotResponse getSlotById(@PathVariable Long slotId);

    @PutMapping("/api/doctors/slots/{slotId}/status")
    SlotResponse updateSlotStatus(@PathVariable Long slotId, @RequestParam boolean isBooked);

    @GetMapping("/api/doctors/userID/{userID}")
    DoctorResponse getDoctorByUserId(@PathVariable Long userID);

    @GetMapping("/api/doctors/id/{id}")
    DoctorResponse getDoctorById(@PathVariable Long id);

}
