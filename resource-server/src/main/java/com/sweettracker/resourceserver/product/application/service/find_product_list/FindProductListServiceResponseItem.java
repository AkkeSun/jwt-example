package com.sweettracker.resourceserver.product.application.service.find_product_list;

import lombok.Builder;

@Builder
public record FindProductListServiceResponseItem(
    Integer id,
    String name
) {

}
