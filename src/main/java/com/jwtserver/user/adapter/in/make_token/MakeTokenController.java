package com.jwtserver.user.adapter.in.make_token;

import com.jwtserver.global.response.ApiResponse;
import com.jwtserver.user.application.port.in.MakeTokenUseCase;
import com.jwtserver.user.application.service.make_token.MakeTokenServiceResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class MakeTokenController {

    private final MakeTokenUseCase useCase;

    @PostMapping("/auth/token")
    ApiResponse<?> makeAccessToken(@RequestBody @Valid MakeTokenRequest request) {
        MakeTokenServiceResponse serviceResponse = useCase.makeAccessToken(request.toCommand());
        return ApiResponse.ok(new MakeTokenResponse().of(serviceResponse));
    }
}
