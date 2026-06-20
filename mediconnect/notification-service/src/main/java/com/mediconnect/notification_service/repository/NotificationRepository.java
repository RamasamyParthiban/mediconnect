package com.mediconnect.notification_service.repository;

import com.mediconnect.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByRecipientEmail(String email);

    List<Notification> findByType(String type);
}
