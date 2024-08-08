package com.sweettracker.resourceserver.product.adapter.in.find_product;

import com.sweettracker.resourceserver.global.response.ApiResponse;
import com.sweettracker.resourceserver.product.application.port.in.FindProductUseCase;
import com.sweettracker.resourceserver.product.application.service.find_product.FindProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductController {

    private final FindProductUseCase findProductUseCase;

    @GetMapping("/products/{id}")
    ApiResponse<FindProductResponse> registerProduct(@PathVariable int id) {
        FindProductServiceResponse serviceResponse = findProductUseCase.findProduct(id);
        return ApiResponse.ok(FindProductResponse.builder()
            .name(serviceResponse.name())
            .price(serviceResponse.price())
            .description(serviceResponse.description())
            .build());
    }
}
