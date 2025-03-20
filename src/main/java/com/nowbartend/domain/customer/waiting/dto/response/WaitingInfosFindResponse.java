package com.nowbartend.domain.customer.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingInfosFindResponse(
    List<WaitingInfoResponse> waitings,
    int curPage,
    int totalPages,
    long totalElements
) {

    public static WaitingInfosFindResponse from(Page<WaitingInfoResponse> waitingInfos) {
        return WaitingInfosFindResponse.builder()
                .waitings(waitingInfos.getContent())
                .curPage(waitingInfos.getNumber())
                .totalPages(waitingInfos.getTotalPages())
                .totalElements(waitingInfos.getTotalElements())
                .build();
    }
}
