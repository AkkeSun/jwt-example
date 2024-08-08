package com.sweettracker.resourceserver.product.application.service.find_product_list;

import java.util.List;
import lombok.Builder;

@Builder
public record FindProductListServiceResponse(
    int totalPage,
    List<FindProductListServiceResponseItem> productList
) {

}
