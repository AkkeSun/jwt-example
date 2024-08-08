package com.sweettracker.resourceserver.product.adapter.in.find_product_list;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductListResponseItem {

    private Integer id;
    private String name;

    @Builder
    FindProductListResponseItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
