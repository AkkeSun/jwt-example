package com.jwtserver.user.application.service.make_token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeTokenServiceResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public MakeTokenServiceResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
