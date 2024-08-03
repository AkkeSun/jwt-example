package com.jwtserver.user.adapter.in.find_user_info;

import com.jwtserver.user.application.service.find_user_info.FindUserInfoServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindUserInfoResponse {

    private String username;
    private String hobby;

    @Builder
    FindUserInfoResponse(String username, String hobby) {
        this.username = username;
        this.hobby = hobby;
    }

    FindUserInfoResponse of(FindUserInfoServiceResponse serviceResponse) {
        return FindUserInfoResponse.builder()
            .username(serviceResponse.getUsername())
            .hobby(serviceResponse.getHobby())
            .build();
    }
}
