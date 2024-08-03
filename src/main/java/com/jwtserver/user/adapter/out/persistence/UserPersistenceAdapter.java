package com.jwtserver.user.adapter.out.persistence;

import com.jwtserver.global.exception.CustomNotFoundException;
import com.jwtserver.global.exception.ErrorCode;
import com.jwtserver.user.application.port.in.command.MakeTokenCommand;
import com.jwtserver.user.application.port.out.FindUserPort;
import com.jwtserver.user.application.port.out.RegisterUserPort;
import com.jwtserver.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort, RegisterUserPort {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
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

        if (!passwordEncoder.matches(command.getPassword(), userEntity.getPassword())) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_USER_INFO);
        }
        return mapper.toDomain(userEntity);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public void registerUser(UserDomain user) {
        userRepository.save(mapper.toEntity(user));
    }

}
