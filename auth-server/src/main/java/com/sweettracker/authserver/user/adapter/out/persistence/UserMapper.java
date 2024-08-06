package com.sweettracker.authserver.user.adapter.out.persistence;

import com.sweettracker.authserver.user.domain.UserDomain;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    public UserDomain toDomain(UserEntity entity) {
        return UserDomain.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .password(entity.getPassword())
            .hobby(entity.getHobby())
            .role(entity.getRole())
            .build();
    }

    public UserEntity toEntity(UserDomain domain) {
        return UserEntity.builder()
            .username(domain.getUsername())
            .password(domain.getPassword())
            .hobby(domain.getHobby())
            .role(domain.getRole())
            .build();
    }

}
