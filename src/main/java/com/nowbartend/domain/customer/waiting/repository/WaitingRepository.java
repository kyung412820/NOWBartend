package com.nowbartend.domain.customer.waiting.repository;

import com.nowbartend.domain.owner.restaurant.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long>, WaitingQueryDslRepository {
}
