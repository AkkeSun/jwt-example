package com.jwtserver.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken {

    private long userId;
    private String username;
    private String userAgent;
    private String refreshToken;

    @Builder
    public RefreshToken(long userId, String username, String userAgent,
        String refreshToken) {
        this.userId = userId;
        this.username = username;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isEmpty() {
        return this.refreshToken.equals("empty");
    }

    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

    public RefreshToken makeEmptyToken() {
        return RefreshToken.builder()
            .refreshToken("empty")
            .build();
    }
}
