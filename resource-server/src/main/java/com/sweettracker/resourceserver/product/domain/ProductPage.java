package com.sweettracker.resourceserver.product.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductPage {

    private List<Product> productList;
    private int totalPage;

    @Builder
    public ProductPage(List<Product> productList, int totalPage) {
        this.productList = productList;
        this.totalPage = totalPage;
    }
}
