package com.sweettracker.authserver.user.adapter.out.persistence;

import com.sweettracker.authserver.global.exception.CustomNotFoundException;
import com.sweettracker.authserver.global.exception.ErrorCode;
import com.sweettracker.authserver.global.util.AesUtil;
import com.sweettracker.authserver.user.application.port.in.command.MakeTokenCommand;
import com.sweettracker.authserver.user.application.port.out.FindUserPort;
import com.sweettracker.authserver.user.domain.Role;
import com.sweettracker.authserver.user.domain.UserDomain;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort {

    private final UserRepository userRepository;
    private final AesUtil aesUtil;
    private final UserMapper mapper;

    @Override
    public UserDomain findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_USER_INFO));
        return mapper.toDomain(userEntity);
    }

    @Override
    public UserDomain findByUsernameAndPassword(MakeTokenCommand command) {
        UserEntity userEntity = userRepository.findByUsername(command.getUsername())
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_USER_INFO));

        if (!aesUtil.matches(command.getPassword(), userEntity.getPassword())) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_USER_INFO);
        }
        return mapper.toDomain(userEntity);
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(UserEntity.builder()
                .username("user")
                .password(aesUtil.encryptText("1234"))
                .hobby("study")
                .role(Role.ROLE_USER)
                .build());
        }

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(UserEntity.builder()
                .username("admin")
                .password(aesUtil.encryptText("1234"))
                .hobby("study")
                .role(Role.ROLE_ADMIN)
                .build());
        }
    }

}
