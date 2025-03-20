package com.nowbartend.domain.customer.reservation.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.notification.event.ReservationEvent;
import com.nowbartend.domain.common.notification.producer.EventProducer;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.service.UserService;
import com.nowbartend.domain.customer.reservation.dto.ReservationCreateRequest;
import com.nowbartend.domain.customer.reservation.dto.ReservationFindResponse;
import com.nowbartend.domain.customer.reservation.entity.Reservation;
import com.nowbartend.domain.customer.reservation.repository.ReservationRepository;
import com.nowbartend.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.nowbartend.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.nowbartend.global.aop.annotation.DistributedLock;
import com.nowbartend.global.exception.DuplicatedResourceException;
import com.nowbartend.global.exception.InvalidException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final AuthService authService;
    private final UserService userService;
    private final RestaurantSeatService restaurantSeatService;
    private final ReservationRepository reservationRepository;
    private final EventProducer eventProducer;

    @DistributedLock(key = "'reservation:' + #request.restaurantId + ':' + #request.seatTypeId + ':' + #formattedDate")
    public Long createReservation(Long userId, ReservationCreateRequest request) {
        User user = userService.findUserById(userId);
        LocalDateTime reservationDate = request.date();

        if (reservationRepository.existsByUserIdAndDate(userId, reservationDate)) {
            throw new DuplicatedResourceException(ErrorCode.DUPLICATE_RESERVATION);
        }

        RestaurantSeat restaurantSeat = restaurantSeatService.findByRestaurantIdAndSeatTypeId(
                request.restaurantId(),
                request.seatTypeId()
        );

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        long reservedCount = reservationRepository.countReservedReservationByRestaurantSeatAndDate(
                restaurantSeat.getId(), reservationDate
        );

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if (restaurantSeat.getQuantity() <= reservedCount) {
            throw new InvalidException(ErrorCode.RESTAURANT_SEAT_NOT_AVAILABLE);
        }

        Integer reservationTotalCount = request.totalCount();

        if (restaurantSeat.getMinGuestCount() > reservationTotalCount) {
            throw new InvalidException(ErrorCode.BELOW_MIN_GUEST_COUNT);
        }

        if (restaurantSeat.getMaxGuestCount() < reservationTotalCount) {
            throw new InvalidException(ErrorCode.EXCEEDS_MAX_GUEST_COUNT);
        }

        Reservation reservation = Reservation.builder()
                .date(reservationDate)
                .totalCount(reservationTotalCount)
                .user(user)
                .restaurantSeat(restaurantSeat)
                .build();

        reservationRepository.save(reservation);

        // 예약 생성 후 이벤트 발행
        ReservationEvent event = new ReservationEvent(reservation.getId(), user.getId());
        eventProducer.sendReservationEvent(event);

        return reservation.getId();
    }

    @Transactional(readOnly = true)
    public ReservationFindResponse findReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);

        return ReservationFindResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationFindResponse> findReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return reservations.stream()
                .map(ReservationFindResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservation(Long userId, Long reservationId) {
        Reservation reservation = findReservedReservationById(reservationId);

        authService.checkUserAuthority(reservation.getUser().getId(), userId);

        reservation.cancel();
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private Reservation findReservedReservationById(Long reservationId) {
        return reservationRepository.findReservedReservationById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVED_RESERVATION_NOT_FOUND));
    }
}
