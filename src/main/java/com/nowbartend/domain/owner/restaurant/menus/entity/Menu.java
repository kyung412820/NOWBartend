package com.nowbartend.domain.owner.restaurant.menus.entity;

import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Menu(
            String name,
            Integer price,
            String description,
            Restaurant restaurant
    ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.restaurant = restaurant;
    }

    public void update(
            String name,
            Integer price,
            String description
    ) {
        updateName(name);
        updatePrice(price);
        updateDescription(description);
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }
}
