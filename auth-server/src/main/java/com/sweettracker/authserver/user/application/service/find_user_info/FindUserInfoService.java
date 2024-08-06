package com.sweettracker.authserver.user.application.service.find_user_info;

import com.sweettracker.authserver.global.util.JwtUtil;
import com.sweettracker.authserver.user.application.port.in.FindUserInfoUseCase;
import com.sweettracker.authserver.user.application.port.out.FindUserPort;
import com.sweettracker.authserver.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindUserInfoService implements FindUserInfoUseCase {

    private final JwtUtil jwtUtil;
    private final FindUserPort findUserPort;

    @Override
    public FindUserInfoServiceResponse findUserInfo(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);
        UserDomain user = findUserPort.findByUsername(username);

        return FindUserInfoServiceResponse.builder()
            .username(user.getUsername())
            .hobby(user.getHobby())
            .build();
    }
}
