package com.sweettracker.authserver.user.adapter.in.make_token;

import com.sweettracker.authserver.user.application.service.make_token.MakeTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeTokenResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public MakeTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    MakeTokenResponse of(MakeTokenServiceResponse serviceResponse) {
        return MakeTokenResponse.builder()
            .accessToken(serviceResponse.getAccessToken())
            .refreshToken(serviceResponse.getRefreshToken())
            .build();
    }
}
