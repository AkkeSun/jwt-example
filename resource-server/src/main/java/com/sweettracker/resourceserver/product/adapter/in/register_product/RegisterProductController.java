package com.sweettracker.resourceserver.product.adapter.in.register_product;

import com.sweettracker.resourceserver.global.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RegisterProductController {

    @PostMapping("/products")
    ApiResponse<String> registerProduct() {
        return ApiResponse.ok("success");
    }
}
