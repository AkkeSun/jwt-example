package com.sweettracker.authserver.user.adapter.out.persistence;

import com.sweettracker.authserver.user.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "HOBBY")
    private String hobby;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    UserEntity(Long id, String username, String password, String hobby,
        Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hobby = hobby;
        this.role = role;
    }
}
