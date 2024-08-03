package com.jwtserver.user.application.service.reissue_token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReIssueTokenServiceResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public ReIssueTokenServiceResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public ReIssueTokenServiceResponse of(String accessToken, String refreshToken) {
        return ReIssueTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
