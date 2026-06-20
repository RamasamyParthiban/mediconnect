package com.mediconnect.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collation = "notifications")
public class Notification {

    @Id
    private String id;

    private String type;

    private String recipientEmail;

    private String subject;

    private String message;

    private boolean sent;

    private LocalDateTime sentAt;
}
