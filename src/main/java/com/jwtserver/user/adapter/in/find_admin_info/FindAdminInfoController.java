package com.jwtserver.user.adapter.in.find_admin_info;

import com.jwtserver.global.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FindAdminInfoController {

    @PostMapping("/api/admin")
    ApiResponse<?> admin() {
        return ApiResponse.ok("success");
    }
}
