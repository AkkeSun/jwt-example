package com.jwtserver.user.application.port.out;

import com.jwtserver.user.application.port.in.command.MakeTokenCommand;
import com.jwtserver.user.domain.UserDomain;

public interface FindUserPort {

    UserDomain findByUsername(String username);

    UserDomain findByUsernameAndPassword(MakeTokenCommand command);

    boolean existByUsername(String username);
}

