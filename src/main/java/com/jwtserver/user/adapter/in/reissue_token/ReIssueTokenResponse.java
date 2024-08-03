package com.jwtserver.user.adapter.in.reissue_token;

import com.jwtserver.user.application.service.reissue_token.ReIssueTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class ReIssueTokenResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    ReIssueTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    ReIssueTokenResponse of(ReIssueTokenServiceResponse serviceResponse) {
        return ReIssueTokenResponse.builder()
            .accessToken(serviceResponse.getAccessToken())
            .refreshToken(serviceResponse.getRefreshToken())
            .build();
    }
}
