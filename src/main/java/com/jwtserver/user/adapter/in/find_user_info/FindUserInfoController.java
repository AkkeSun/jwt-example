package com.jwtserver.user.adapter.in.find_user_info;

import com.jwtserver.global.response.ApiResponse;
import com.jwtserver.user.application.port.in.FindUserInfoUseCase;
import com.jwtserver.user.application.service.find_user_info.FindUserInfoServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindUserInfoController {

    private final FindUserInfoUseCase findUserInfoUseCase;

    @PostMapping("/api/users")
    ApiResponse<FindUserInfoResponse> findUserInfo(
        @RequestHeader("Authorization") String accessToken) {
        log.info("POST /api/users");
        FindUserInfoServiceResponse serviceResponse = findUserInfoUseCase.findUserInfo(accessToken);
        return ApiResponse.ok(new FindUserInfoResponse().of(serviceResponse));
    }
}
