package com.nowbartend.domain.customer.post.region.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "regions")
@Getter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
