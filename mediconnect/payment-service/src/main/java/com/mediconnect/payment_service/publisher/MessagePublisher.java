package com.mediconnect.payment_service.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE = "mediconnect.exchange";

    public void publishPaymentEvent(Object event, String routeKey) {

        rabbitTemplate.convertAndSend(EXCHANGE, routeKey, event);
    }
}
