package com.sweettracker.resourceserver.product.adapter.in.find_product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductResponse {

    private String name;
    private int price;
    private String description;

    @Builder
    FindProductResponse(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
