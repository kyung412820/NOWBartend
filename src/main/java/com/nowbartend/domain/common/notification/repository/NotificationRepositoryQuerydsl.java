package com.nowbartend.domain.common.notification.repository;

import com.nowbartend.domain.common.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NotificationRepositoryQuerydsl {

    Page<Notification> findDueNotifications(LocalDateTime currentTime, Pageable pageable);
    int updateIsSentTrue(Long id);
}
