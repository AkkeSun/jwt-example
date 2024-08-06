package com.sweettracker.authserver.user.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "USER_REFRESH_TOKEN")
@NoArgsConstructor
class UserRefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Builder
    public UserRefreshTokenEntity(Long id, String username, String userAgent, String refreshToken,
        LocalDateTime regDate) {
        this.id = id;
        this.username = username;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.regDate = regDate;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateRegDate() {
        this.regDate = LocalDateTime.now();
    }
}
