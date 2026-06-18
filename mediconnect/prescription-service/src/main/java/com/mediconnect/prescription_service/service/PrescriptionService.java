package com.mediconnect.prescription_service.service;

import com.mediconnect.prescription_service.dto.PrescriptionRequest;
import com.mediconnect.prescription_service.dto.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest);

    PrescriptionResponse getPrescriptionById(Long id);

    List<PrescriptionResponse> getAllPrescriptionsForPatient();

    List<PrescriptionResponse> getAllPrescriptionsForDoctor();

    PrescriptionResponse getPrescriptionByAppointmentId(Long appointmentId);

    PrescriptionResponse updatePrescription(Long prescriptionId, PrescriptionRequest prescriptionRequest);

}
