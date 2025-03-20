package com.nowbartend.global.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "reservationExchange";
    public static final String QUEUE = "reservationQueue";
    public static final String ROUTING_KEY = "reservation.created";

    // 익스체인지 생성 (TopicExchange를 사용)
    @Bean
    public TopicExchange reservationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    // 큐 생성
    @Bean
    public Queue reservationQueue() {
        return new Queue(QUEUE);
    }

    // 큐와 익스체인지를 라우팅 키로 바인딩
    @Bean
    public Binding bindingReservation(Queue reservationQueue, TopicExchange reservationExchange) {
        return BindingBuilder.bind(reservationQueue).to(reservationExchange).with(ROUTING_KEY);
    }

    // JSON 직렬화를 위한 MessageConverter 설정
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate에 MessageConverter를 설정 (Spring Boot는 기본적으로 RabbitTemplate을 생성함)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}