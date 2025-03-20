package com.nowbartend.domain.common.notification.scheduler;

import com.nowbartend.domain.common.notification.entity.Notification;
import com.nowbartend.domain.common.notification.repository.NotificationRepository;
import com.nowbartend.domain.common.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    // 5분마다 현재 시간이 전송해야하는 시간을 지난 것들 메일 보내기
    @Scheduled(cron = "0 */5 * * * ?")
    public void processDueNotifications() {

        int page = 0;
        // 한번에 가져올 데이터 배치 사이즈
        int size = 100;

        Page<Notification> notifications;

        // 데이터를 모두 한번에 가져오지 않고 페이지네이션을 통해 잘게잘게 가져와서 처리
        do {
            notifications = notificationRepository.findDueNotifications(LocalDateTime.now(), PageRequest.of(page, size));
            notifications.forEach(notificationService::sendReservationEmail);
            page++;
        } while (notifications.hasNext());
    }
}
