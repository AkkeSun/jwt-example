package com.jwtserver.user.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, Long> {

    Optional<UserRefreshTokenEntity> findByUsernameAndUserAgent(String username, String userAgent);

    @Modifying
    @Query("update UserRefreshTokenEntity t set t.refreshToken=:refreshToken where t.username=:username and t.userAgent = :userAgent")
    void updateRefreshToken(String refreshToken, String username, String userAgent);

    @Modifying
    @Query("delete UserRefreshTokenEntity t where t.username=:username and t.userAgent=:userAgent")
    void deleteRefreshToken(String username, String userAgent);
}
