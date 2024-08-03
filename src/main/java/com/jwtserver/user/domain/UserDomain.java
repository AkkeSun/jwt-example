package com.jwtserver.user.domain;

import com.jwtserver.user.adapter.out.redis.dto.RedisToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDomain {

    private Long id;

    private String username;

    private String password;

    private String hobby;

    private Role role;


    @Builder
    public UserDomain(Long id, String username, String password, String hobby, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hobby = hobby;
        this.role = role;
    }

    public UserDomain of(RedisToken redisToken) {
        return UserDomain.builder()
            .id(redisToken.getUserId())
            .username(redisToken.getUsername())
            .role(Role.valueOf(redisToken.getRole()))
            .build();
    }
    
}
