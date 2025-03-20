package com.nowbartend.domain.owner.restaurant.menus.dto.request;

import com.nowbartend.domain.owner.restaurant.menus.entity.Menu;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuCreateRequest(
        @NotBlank
        String menuName,

        @NotNull
        @Min(0)
        Integer menuPrice,

        @Size(max = 100)
        String menuDescription
) {

    public Menu toEntity(Restaurant restaurant) {
        return Menu.builder()
                .name(this.menuName)
                .price(this.menuPrice)
                .description(this.menuDescription)
                .restaurant(restaurant)
                .build();
    }
}
