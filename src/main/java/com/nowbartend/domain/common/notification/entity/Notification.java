package com.nowbartend.domain.common.notification.entity;

import com.nowbartend.domain.common.BaseEntity;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.customer.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@DynamicInsert
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String headerMessage;

    // 알림 보내야할 시간
    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @ColumnDefault(value = "false")
    private boolean isSent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Notification(String headerMessage, String email, LocalDateTime scheduledTime, User user, Reservation reservation) {
        this.headerMessage = headerMessage;
        this.scheduledTime = scheduledTime;
        this.user = user;
        this.reservation = reservation;
    }

    public void updateSent() {
        this.isSent = true;
    }
}
