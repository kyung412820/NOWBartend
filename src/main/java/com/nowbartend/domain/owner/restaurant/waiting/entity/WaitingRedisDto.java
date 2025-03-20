package com.nowbartend.domain.owner.restaurant.waiting.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingRedisDto {

    private Long userId;
    private Integer totalCount;
    private WaitingType waitingType;

    @Builder(access = AccessLevel.PRIVATE)
    public WaitingRedisDto(Long userId, Integer totalCount, WaitingType waitingType) {
        this.userId = userId;
        this.totalCount = totalCount;
        this.waitingType = waitingType;
    }

    public static WaitingRedisDto from(Waiting waiting) {
        return WaitingRedisDto.builder()
                .userId(waiting.getUser().getId())
                .totalCount(waiting.getTotalCount())
                .waitingType(waiting.getType())
                .build();
    }
}
