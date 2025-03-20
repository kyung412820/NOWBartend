package com.nowbartend.domain.admin.seatType.service;

import com.nowbartend.domain.admin.seatType.entity.SeatType;
import com.nowbartend.domain.admin.seatType.repository.SeatTypeRepository;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;

    public SeatType findSeatTypeById(Long seatTypeId) {
        return seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.SEAT_TYPE_NOT_FOUND));
    }

    public SeatType findSeatTypeByName(String seatTypeName) {
        return seatTypeRepository.findByName(seatTypeName)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.SEAT_TYPE_NAME_NOT_FOUND));
    }
}
