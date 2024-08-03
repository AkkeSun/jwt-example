package com.jwtserver.user.application.service.find_user_info;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindUserInfoServiceResponse {

    private String username;
    private String hobby;

    @Builder
    public FindUserInfoServiceResponse(String username, String hobby) {
        this.username = username;
        this.hobby = hobby;
    }
}
