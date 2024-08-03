package com.jwtserver.user.application.port.in.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReIssueTokenCommand {

    private String refreshToken;

    @Builder
    public ReIssueTokenCommand(String accessToken, String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
