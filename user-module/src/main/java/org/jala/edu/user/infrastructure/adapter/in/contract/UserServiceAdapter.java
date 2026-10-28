package org.jala.edu.user.infrastructure.adapter.in.contract;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jala.edu.core.contract.UserService;
import org.jala.edu.user.application.port.in.CheckUserAccessQuery;
import org.jala.edu.user.application.usecase.CheckUserAccessUseCase;
import org.jala.edu.user.domain.model.UserRole;

/**
 * Inbound adapter that serves the {@link UserService} contract to other modules through this module's use case.
 * The contract's roles are translated by name, so the user domain keeps its own {@link UserRole}.
 */
public class UserServiceAdapter implements UserService {

    private final CheckUserAccessUseCase checkUserAccessUseCase;

    public UserServiceAdapter(CheckUserAccessUseCase checkUserAccessUseCase) {
        this.checkUserAccessUseCase = checkUserAccessUseCase;
    }

    @Override
    public boolean isActiveWithRole(UUID userId, org.jala.edu.core.contract.UserRole... roles) {
        Set<UserRole> userRoles = Arrays.stream(roles)
                .map(role -> UserRole.valueOf(role.name()))
                .collect(Collectors.toSet());
        return checkUserAccessUseCase.execute(new CheckUserAccessQuery(userId, userRoles));
    }
}
