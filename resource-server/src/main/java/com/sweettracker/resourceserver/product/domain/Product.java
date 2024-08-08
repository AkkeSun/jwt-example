package com.sweettracker.resourceserver.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Integer id;

    private String name;

    private int price;

    private String description;

    @Builder
    public Product(Integer id, String name, int price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
