package com.jwtserver.user.adapter.in.reissue_token;

import com.jwtserver.user.application.port.in.command.ReIssueTokenCommand;
import javax.validation.constraints.NotBlank;
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
