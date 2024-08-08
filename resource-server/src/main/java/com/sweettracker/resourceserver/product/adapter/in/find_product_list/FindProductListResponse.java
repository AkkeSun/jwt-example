package com.sweettracker.resourceserver.product.adapter.in.find_product_list;

import com.sweettracker.resourceserver.product.application.service.find_product_list.FindProductListServiceResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductListResponse {

    private int totalPage;
    private List<FindProductListResponseItem> productList;

    @Builder
    FindProductListResponse(int totalPage, List<FindProductListResponseItem> productList) {
        this.totalPage = totalPage;
        this.productList = productList;
    }

    FindProductListResponse of(FindProductListServiceResponse serviceResponse) {
        return FindProductListResponse.builder()
            .totalPage(serviceResponse.totalPage())
            .productList(serviceResponse.productList().stream()
                .map(product -> FindProductListResponseItem.builder()
                    .id(product.id())
                    .name(product.name())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }
}
