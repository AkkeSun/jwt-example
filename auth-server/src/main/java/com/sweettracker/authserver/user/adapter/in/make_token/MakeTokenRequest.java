package com.sweettracker.authserver.user.adapter.in.make_token;

import com.sweettracker.authserver.user.application.port.in.command.MakeTokenCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeTokenRequest {

    @NotBlank(message = "이름은 필수값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;

    @Builder
    public MakeTokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    MakeTokenCommand toCommand() {
        return MakeTokenCommand.builder()
            .username(username)
            .password(password)
            .build();
    }
}
