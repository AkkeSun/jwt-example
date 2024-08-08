package com.sweettracker.resourceserver.product.application.service.find_product;

import lombok.Builder;

@Builder
public record FindProductServiceResponse(

    String name,
    int price,
    String description
) {

}
