package com.nowbartend.domain.customer.post.region.repository;

import com.nowbartend.domain.customer.post.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
