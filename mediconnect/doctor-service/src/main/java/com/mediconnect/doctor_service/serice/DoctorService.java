package com.mediconnect.doctor_service.serice;

import com.mediconnect.doctor_service.dto.DoctorRequest;
import com.mediconnect.doctor_service.dto.DoctorResponse;
import com.mediconnect.doctor_service.dto.SlotRequest;
import com.mediconnect.doctor_service.dto.SlotResponse;


import java.util.List;

public interface DoctorService {

    DoctorResponse registerDoctor(DoctorRequest doctorRequest);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse getDoctorByUserId(Long userId);

    DoctorResponse updateDoctorProfile(DoctorRequest doctorRequest);

    SlotResponse addAvailabilitySlot (SlotRequest slotRequest);

    List<SlotResponse> getAllSlotsForDoctor(Long doctorID);

    List<SlotResponse> getAvailableSlots(Long doctorID, boolean isBooked);

    List<DoctorResponse> getDoctorsBySpecialization (String specialization);

    List<DoctorResponse> getAllDoctors();

}



//Doctor profile:
//        → create doctor profile
//→ get doctor by id
//→ get doctor by userId (from JWT)
//→ update doctor profile
//
//Slot management:
//        → add availability slot
//→ get all slots for a doctor
//→ get available slots only (isBooked = false)
//
//Search:
//        → search doctors by specialization
//→ get all doctors