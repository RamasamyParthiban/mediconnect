package com.mediconnect.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Exchange name
    public static final String EXCHANGE = "mediconnect.exchange";

    //Queue names

    public static final String APPOINTMENT_BOOKED_QUEUE = "notification.appointment.queue";

    public static final String APPOINTMENT_CANCELLED_QUEUE = "notification.appointment.cancelled.queue";

    public static final String PAYMENT_QUEUE = "notification.payment.queue";

    public static final String PRESCRIPTION_QUEUE = "notification.prescription.queue";

    //Routing Keys

    public static final String APPOINTMENT_BOOKED_ROUTING_KEY = "appointment.booked";

    public static final String APPOINTMENT_CANCELLED_ROUTING_KEY = "appointment.cancelled";

    public static final String PAYMENT_ROUTING_KEY = "payment.success";

    public static final String PRESCRIPTION_ROUTING_KEY = "prescription.created";

    //Declare Exchange

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    //Declare Queues
    @Bean
    public Queue appointmentBookedQueue() {
        return new Queue(APPOINTMENT_BOOKED_QUEUE);
    }

    @Bean
    public Queue appointmentCancelledQueue(){
        return new Queue(APPOINTMENT_CANCELLED_QUEUE);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public Queue prescriptionQueue() {
        return new Queue(PRESCRIPTION_QUEUE);
    }

    //Bind Queues to Exchange
    @Bean
    public Binding appointmentBookedBinding() {

        return BindingBuilder
                .bind(appointmentBookedQueue())
                .to(exchange())
                .with(APPOINTMENT_BOOKED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder
                .bind(paymentQueue())
                .to(exchange())
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding prescriptionBinding() {
        return BindingBuilder
                .bind(prescriptionQueue())
                .to(exchange())
                .with(PRESCRIPTION_ROUTING_KEY);
    }

    @Bean
    public Binding appointmentCancelledBinding(){

        return BindingBuilder
                .bind(appointmentCancelledQueue())
                .to(exchange())
                .with(APPOINTMENT_CANCELLED_ROUTING_KEY);
    }

    //Json Message Converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //RabbitTemplate with JSON converter

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }
}
