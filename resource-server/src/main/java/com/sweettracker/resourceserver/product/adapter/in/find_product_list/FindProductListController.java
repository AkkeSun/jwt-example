package com.sweettracker.resourceserver.product.adapter.in.find_product_list;

import com.sweettracker.resourceserver.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FindProductListController {

    @GetMapping("/products")
    ApiResponse<String> findProductList() {
        return ApiResponse.ok("success");
    }
}
