package com.nowbartend.domain.common.notification.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.customer.reservation.entity.Reservation;
import com.nowbartend.domain.customer.reservation.repository.ReservationRepository;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import com.nowbartend.domain.common.notification.entity.Notification;
import com.nowbartend.domain.common.notification.entity.NotificationType;
import com.nowbartend.domain.common.notification.event.ReservationEvent;
import com.nowbartend.domain.common.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void createReservationNotification(ReservationEvent event) {

        // 즉시 전송 알림
        createNotification(event, NotificationType.CONFIRMATION, null);

        // 리마인더 알림 (24시간 전, 1시간 전)
        createNotification(event, NotificationType.REMINDER, 24);
        createNotification(event, NotificationType.REMINDER, 1);
    }

    private void createNotification(ReservationEvent event, NotificationType type, Integer hoursBefore) {
        LocalDateTime scheduledTime;
        String headerMessage;

        User user = findUserById(event.userId());
        Reservation reservation = findReservationById(event.reservationId());

        switch (type) {
            case CONFIRMATION ->
            {
                scheduledTime = LocalDateTime.now();
                headerMessage = "예약이 확정되었습니다.";
            }
            case REMINDER ->
            {
                scheduledTime = reservation.getDate().minusHours(hoursBefore);

                if (!scheduledTime.isAfter(LocalDateTime.now())) {
                    return; // 이미 예약 시간이 지난 경우 생성하지 않음.
                }

                headerMessage = (hoursBefore == 24 ? "하루 전 알림:" : hoursBefore + "시간 전 알림:");
            }
            default -> throw new IllegalArgumentException("알 수 없는 알림 타입: " + type);
        }

        Notification notification = Notification.builder()
                .headerMessage(headerMessage)
                .scheduledTime(scheduledTime)
                .user(user)
                .reservation(reservation)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void sendReservationEmail(Notification notification) {

        int updateRows = notificationRepository.updateIsSentTrue(notification.getId());

        // 업데이트 된 알림에 대해서만 메일 전송
        if(updateRows > 0) {

            emailService.sendEmail(notification);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
    }
}
