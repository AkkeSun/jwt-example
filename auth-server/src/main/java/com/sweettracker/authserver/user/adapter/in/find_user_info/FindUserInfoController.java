package com.sweettracker.authserver.user.adapter.in.find_user_info;

import com.sweettracker.authserver.global.response.ApiResponse;
import com.sweettracker.authserver.user.application.port.in.FindUserInfoUseCase;
import com.sweettracker.authserver.user.application.service.find_user_info.FindUserInfoServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindUserInfoController {

    private final FindUserInfoUseCase findUserInfoUseCase;

    @PostMapping("/api/users")
    ApiResponse<FindUserInfoResponse> findUserInfo(String accessToken) {
        FindUserInfoServiceResponse serviceResponse = findUserInfoUseCase.findUserInfo(accessToken);
        return ApiResponse.ok(new FindUserInfoResponse().of(serviceResponse));
    }
}
