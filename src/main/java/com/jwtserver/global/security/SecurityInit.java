package com.jwtserver.global.security;

import com.jwtserver.user.application.port.out.FindUserPort;
import com.jwtserver.user.application.port.out.RegisterUserPort;
import com.jwtserver.user.domain.Role;
import com.jwtserver.user.domain.UserDomain;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final FindUserPort findUserPort;
    private final RegisterUserPort registerUserPort;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!findUserPort.existByUsername("user")) {
            // 시큐리티 계정은 항상 password 가 암호화 되어있어야 합니다.
            registerUserPort.registerUser(UserDomain.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .hobby("study")
                .role(Role.ROLE_USER)
                .build());
        }
    }
}
