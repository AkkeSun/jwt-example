package com.jwtserver.user.adapter.out.persistence;

import com.jwtserver.user.application.port.out.DeleteTokenPort;
import com.jwtserver.user.application.port.out.FindTokenPort;
import com.jwtserver.user.application.port.out.RegisterTokenPort;
import com.jwtserver.user.application.port.out.UpdateTokenPort;
import com.jwtserver.user.domain.RefreshToken;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
class TokenPersistenceAdapter implements FindTokenPort, RegisterTokenPort,
    UpdateTokenPort, DeleteTokenPort {

    private final UserRefreshTokenRepository tokenRepository;

    private final TokenMapper mapper;

    @Override
    public void deleteToken(String username, String userAgent) {
        tokenRepository.deleteRefreshToken(username, userAgent);
    }

    @Override
    public RefreshToken findRefreshToken(String username, String userAgent) {
        Optional<UserRefreshTokenEntity> optional = tokenRepository
            .findByUsernameAndUserAgent(username, userAgent);
        if (optional.isPresent()) {
            return mapper.toDomain(optional.get());
        }
        return new RefreshToken().makeEmptyToken();
    }

    @Override
    public void registerRefreshToken(String username, String userAgent,
        String refreshToken) {
        tokenRepository.save(UserRefreshTokenEntity.builder()
            .username(username)
            .refreshToken(refreshToken)
            .userAgent(userAgent)
            .build());
    }

    @Override
    public void updateRefreshToken(RefreshToken refreshToken) {
        tokenRepository.updateRefreshToken(refreshToken.getRefreshToken(),
            refreshToken.getUsername(), refreshToken.getUserAgent());
    }
}
