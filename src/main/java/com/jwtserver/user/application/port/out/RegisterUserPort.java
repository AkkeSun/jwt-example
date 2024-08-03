package com.jwtserver.user.application.port.out;

import com.jwtserver.user.domain.UserDomain;

public interface RegisterUserPort {

    void registerUser(UserDomain user);
}
