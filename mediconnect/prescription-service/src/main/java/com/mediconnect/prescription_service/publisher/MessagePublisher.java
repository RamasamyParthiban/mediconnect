package com.mediconnect.prescription_service.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE ="mediconnect.exchange";

    public void publishPrescriptionEvent(Object event, String routeKey){

        rabbitTemplate.convertAndSend(EXCHANGE, routeKey, event);
    }
}
