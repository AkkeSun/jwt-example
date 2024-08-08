package com.sweettracker.resourceserver.product.adapter.in.find_product_list;

import com.sweettracker.resourceserver.global.response.ApiResponse;
import com.sweettracker.resourceserver.product.application.port.in.FindProductListUseCase;
import com.sweettracker.resourceserver.product.application.service.find_product_list.FindProductListServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindProductListController {

    private final FindProductListUseCase findProductListUseCase;

    @GetMapping("/products")
    ApiResponse<FindProductListResponse> findProductList(
        @RequestParam(defaultValue = "0") int page) {
        log.info("findProductList call");

        FindProductListServiceResponse serviceResponses = findProductListUseCase.findProductList(
            page);
        return ApiResponse.ok(new FindProductListResponse().of(serviceResponses));
    }
}
