package com.sweettracker.authserver.user.application.port.in.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeTokenCommand {

    private String username;

    private String password;

    @Builder
    public MakeTokenCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
