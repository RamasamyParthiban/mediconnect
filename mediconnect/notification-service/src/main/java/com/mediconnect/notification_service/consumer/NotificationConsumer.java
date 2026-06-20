package com.mediconnect.notification_service.consumer;

import com.mediconnect.notification_service.config.RabbitMQConfig;
import com.mediconnect.notification_service.event.AppointmentEvent;
import com.mediconnect.notification_service.event.PaymentEvent;
import com.mediconnect.notification_service.event.PrescriptionEvent;
import com.mediconnect.notification_service.model.Notification;
import com.mediconnect.notification_service.repository.NotificationRepository;
import com.mediconnect.notification_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConsumer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.APPOINTMENT_BOOKED_QUEUE)
    public void handleAppointmentEvent(AppointmentEvent event) {

        String subject = "Appointment " + event.getStatus();

        String message = "Dear Patient, \n\n" +
                "Your appointment with Dr. " + event.getDoctorName() +
                " has been " + event.getStatus() + ".\n" +
                "Appointment Time: " + event.getAppointmentTime() + "\n\n" +
                "Thank you, \nMediConnect Team";

        emailService.sendEmail(event.getPatientEmail(), subject, message);

        notificationRepository.save(Notification
                .builder()
                .type("APPOINTMENT BOOKED")
                .recipientEmail(event.getPatientEmail())
                .subject(subject)
                .message(message)
                .sent(true)
                .sentAt(LocalDateTime.now())
                .build());
    }

    @RabbitListener(queues = RabbitMQConfig.APPOINTMENT_CANCELLED_QUEUE)
    public void handleAppointmentCancelledQueue(AppointmentEvent event) {

        String subject = "Appointment Cancelled - MediConnect";

        String message = "Dear Patient, \n\n" +
                "Your Appointment with Dr. " + event.getDoctorName() +
                " has been CANCELLED.\n\n" +
                "Please book a new appointment at your convenience.\n\n" +
                "Thank you, \nMediConnect Team";

        emailService.sendEmail(event.getPatientEmail(), subject, message);

        notificationRepository.save(Notification
                .builder()
                .type("APPOINTMENT CANCELLED")
                .recipientEmail(event.getPatientEmail())
                .subject(subject)
                .message(message)
                .sent(true)
                .sentAt(LocalDateTime.now())
                .build());


    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePaymentEvent(PaymentEvent event) {

        String subject = "Payment Successful - MediConnect";

        String message = "Dear Patient, \n\n" +
                "Your Payment for Rs. " + event.getAmount() +
                " was successful!\n" +
                "Transaction Id: " + event.getTransactionId() + "\n" +
                "Payment Method: " + event.getPaymentMethod() + "\n\n" +
                "Thank you, \nMediConnect Team";

        emailService.sendEmail(event.getPatientEmail(), subject, message);

        notificationRepository.save(Notification
                .builder()
                .type("PAYMENT")
                .recipientEmail(event.getPatientEmail())
                .subject(subject)
                .message(message)
                .sent(true)
                .sentAt(LocalDateTime.now())
                .build());
    }

    @RabbitListener(queues = RabbitMQConfig.PRESCRIPTION_QUEUE)
    public void handlePrescriptionEvent(PrescriptionEvent event) {

        String subject = "New Prescription - MediConnect";

        String message = "Dear Patient, \n\n" +
                "Dr. " + event.getDoctorName() +
                " has created a prescription for you.\n" +
                "Instructions: " + event.getInstructions() + "\n\n" +
                "Thank you, \nMediConnect Team";

        emailService.sendEmail(event.getPatientEmail(), subject, message);

        notificationRepository.save(Notification.builder()
                .type("PRESCRIPTION")
                .recipientEmail(event.getPatientEmail())
                .subject(subject)
                .message(message)
                .sent(true)
                .sentAt(LocalDateTime.now())
                .build());
    }
}
