package com.jwtserver.user.adapter.out.persistence;

import com.jwtserver.user.domain.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
