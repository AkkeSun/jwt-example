package com.jwtserver.user.application.service.find_user_info;

import com.jwtserver.global.security.JwtAuthenticationProvider;
import com.jwtserver.user.application.port.in.FindUserInfoUseCase;
import com.jwtserver.user.application.port.out.FindUserPort;
import com.jwtserver.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindUserInfoService implements FindUserInfoUseCase {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final FindUserPort findUserPort;

    @Override
    public FindUserInfoServiceResponse findUserInfo(String accessToken) {
        String username = jwtAuthenticationProvider.getUsername(accessToken);
        UserDomain user = findUserPort.findByUsername(username);

        return FindUserInfoServiceResponse.builder()
            .username(user.getUsername())
            .hobby(user.getHobby())
            .build();
    }
}
