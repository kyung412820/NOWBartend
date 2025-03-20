package com.nowbartend.domain.admin.seatType.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public SeatType(String name) {
        this.name = name;
    }
}
