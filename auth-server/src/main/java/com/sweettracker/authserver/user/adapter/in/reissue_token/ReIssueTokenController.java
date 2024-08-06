package com.sweettracker.authserver.user.adapter.in.reissue_token;

import com.sweettracker.authserver.global.response.ApiResponse;
import com.sweettracker.authserver.user.application.port.in.ReIssueTokenUseCase;
import com.sweettracker.authserver.user.application.service.reissue_token.ReIssueTokenServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class ReIssueTokenController {

    private final ReIssueTokenUseCase useCase;

    @PostMapping("/auth/refresh-token")
    ApiResponse<?> makeAccessToken(@RequestBody @Valid ReIssueTokenRequest request) {
        ReIssueTokenServiceResponse serviceResponse = useCase
            .reIssueToken(request.getRefreshToken());
        return ApiResponse.ok(new ReIssueTokenResponse().of(serviceResponse));
    }
}
