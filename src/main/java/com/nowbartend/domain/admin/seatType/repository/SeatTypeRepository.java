package com.nowbartend.domain.admin.seatType.repository;

import com.nowbartend.domain.admin.seatType.entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {

    Optional<SeatType> findByName(String seatTypeName);

}
