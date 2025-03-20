package com.nowbartend.domain.owner.restaurant.waiting.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingStatus;
import com.nowbartend.domain.owner.restaurant.waiting.entity.WaitingType;

public record WaitingDetails(
        Long waitingId,
        Long userId,
        String userName,
        Integer totalCount,
        String userPhoneNumber,
        WaitingStatus status,
        WaitingType type,
        Integer sequence
) {

    @QueryProjection
    public WaitingDetails {
    }
}
