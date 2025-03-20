package com.nowbartend.domain.customer.waiting.repository;

import com.nowbartend.domain.customer.waiting.dto.response.WaitingInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaitingQueryDslRepository {

    Page<WaitingInfoResponse> findWaitingInfo(Long userId, Pageable pageable);
}
