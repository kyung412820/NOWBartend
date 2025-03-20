package com.nowbartend.domain.owner.restaurant.menus.dto.response;

import com.nowbartend.domain.owner.restaurant.menus.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MenuFindResponse(
        List<MenuDetailResponse> menus
) {

    public static MenuFindResponse from(List<Menu> menus) {
        List<MenuDetailResponse> menuResponse = menus.stream()
                .map(MenuDetailResponse::from)
                .toList();

        return MenuFindResponse.builder()
                .menus(menuResponse)
                .build();
    }
}
