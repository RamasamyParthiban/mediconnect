package com.mediconnect.payment_service.controller;

import com.mediconnect.payment_service.dto.PaymentRequest;
import com.mediconnect.payment_service.dto.PaymentResponse;
import com.mediconnect.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory() {

        return ResponseEntity.ok(paymentService.getPaymentHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id){
        try{
            PaymentResponse payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<?> getPaymentByAppointmentId(@PathVariable Long id){
        try{
            PaymentResponse payment = paymentService.getPaymentByAppointmentID(id);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String id){
        try{
            PaymentResponse payment = paymentService.getPaymentByTransactionId(id);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
