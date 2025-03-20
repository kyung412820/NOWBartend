package com.nowbartend.domain.owner.restaurant.menus.controller;

import com.nowbartend.domain.owner.restaurant.menus.dto.request.MenuCreateRequest;
import com.nowbartend.domain.owner.restaurant.menus.dto.request.MenuUpdateRequest;
import com.nowbartend.domain.owner.restaurant.menus.service.MenuService;
import com.nowbartend.global.security.entity.CustomUserDetails;
import com.nowbartend.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owner/api/restaurants/{restaurantId}/menus")
@RequiredArgsConstructor
public class OwnerMenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Void> createMenu(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId,
            @RequestBody List<@Valid MenuCreateRequest> request
    ) {
        menuService.createMenu(userDetails.getId(), restaurantId, request);

        URI location = UriBuilderUtil.create("/owner/api/restaurants/{restaurantId}", restaurantId);

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @RequestBody @Valid MenuUpdateRequest request
    ) {
        menuService.updateMenu(userDetails.getId(), restaurantId, menuId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(userDetails.getId(), restaurantId, menuId);

        return ResponseEntity.noContent().build();
    }
}
