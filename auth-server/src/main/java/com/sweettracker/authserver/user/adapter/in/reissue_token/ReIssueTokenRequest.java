package com.sweettracker.authserver.user.adapter.in.reissue_token;

import com.sweettracker.authserver.user.application.port.in.command.ReIssueTokenCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReIssueTokenRequest {

    @NotBlank(message = "리프레시 토큰은 필수값 입니다.")
    private String refreshToken;

    @Builder
    public ReIssueTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    ReIssueTokenCommand toCommand() {
        return ReIssueTokenCommand.builder()
            .refreshToken(refreshToken)
            .build();
    }
}
