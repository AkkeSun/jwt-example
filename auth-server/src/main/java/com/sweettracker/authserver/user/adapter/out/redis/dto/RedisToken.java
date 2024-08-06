package com.sweettracker.authserver.user.adapter.out.redis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sweettracker.authserver.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RedisToken {

    private long userId;
    private String username;
    private String role;
    private String refreshToken;

    @Builder
    public RedisToken(long userId, String username, String role, String refreshToken) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public RedisToken of(UserDomain user, String refreshToken) {
        return RedisToken.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .role(user.getRole().name())
            .refreshToken(refreshToken)
            .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonIgnore
    public boolean isCircuitBreakerResponse() {
        return this.refreshToken.equals("redisError");
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.refreshToken.equals("empty");
    }

    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }
}
