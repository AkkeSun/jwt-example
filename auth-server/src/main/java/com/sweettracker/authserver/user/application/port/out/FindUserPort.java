package com.sweettracker.authserver.user.application.port.out;

import com.sweettracker.authserver.user.application.port.in.command.MakeTokenCommand;
import com.sweettracker.authserver.user.domain.UserDomain;

public interface FindUserPort {

    UserDomain findByUsername(String username);

    UserDomain findByUsernameAndPassword(MakeTokenCommand command);
}

