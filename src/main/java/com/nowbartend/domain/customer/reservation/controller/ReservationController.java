package com.nowbartend.domain.customer.reservation.controller;

import com.nowbartend.domain.customer.reservation.dto.ReservationCreateRequest;
import com.nowbartend.domain.customer.reservation.dto.ReservationFindResponse;
import com.nowbartend.domain.customer.reservation.service.ReservationService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid ReservationCreateRequest request
    ) {
        Long reservationId = reservationService.createReservation(userDetails.getId(), request);

        URI location = UriBuilderUtil.create("/customer/api/reservations/{reservationId}", reservationId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationFindResponse> findReservation(
            @PathVariable Long reservationId
    ) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @GetMapping
    public ResponseEntity<List<ReservationFindResponse>> findReservations(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(reservationService.findReservations(userDetails.getId()));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reservationId
    ) {
        reservationService.deleteReservation(userDetails.getId(), reservationId);

        return ResponseEntity.noContent().build();
    }
}



