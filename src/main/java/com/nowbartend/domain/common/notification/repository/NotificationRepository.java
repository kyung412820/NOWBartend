package com.nowbartend.domain.common.notification.repository;

import com.nowbartend.domain.common.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> , NotificationRepositoryQuerydsl{


}
