package com.nowbartend.domain.owner.restaurant.waiting.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record WaitingQueueFindUsersResponse(
        List<WaitingDetails> users,
        int curPage,
        int totalPages,
        long totalElements
) {

    public static WaitingQueueFindUsersResponse from(List<WaitingDetails> userDetails, int curPage, int totalPages, Long totalElements) {
        return WaitingQueueFindUsersResponse.builder()
                .users(userDetails)
                .curPage(curPage)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }
}
