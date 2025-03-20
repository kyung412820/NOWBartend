package com.nowbartend.domain.owner.restaurant.menus.repository;

import com.nowbartend.domain.owner.restaurant.menus.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByRestaurantId(Long restaurantId);

    Optional<Menu> findByIdAndRestaurantId(Long id, Long restaurantId);
}
