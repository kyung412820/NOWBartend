package com.nowbartend.domain.common.notification.listener;

import com.nowbartend.domain.common.notification.event.ReservationEvent;
import com.nowbartend.domain.common.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationNotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "reservationQueue")
    public void handleReservationNotification(ReservationEvent event) {
        notificationService.createReservationNotification(event);
    }

}