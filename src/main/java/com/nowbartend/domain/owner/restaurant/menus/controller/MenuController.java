package com.nowbartend.domain.owner.restaurant.menus.controller;

import com.nowbartend.domain.owner.restaurant.menus.dto.response.MenuDetailResponse;
import com.nowbartend.domain.owner.restaurant.menus.dto.response.MenuFindResponse;
import com.nowbartend.domain.owner.restaurant.menus.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<MenuFindResponse> findMenus(
            @PathVariable Long restaurantId
    ) {
        return ResponseEntity.ok(menuService.findMenus(restaurantId));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> findMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ) {
        return ResponseEntity.ok(menuService.findMenu(restaurantId, menuId));
    }
}
