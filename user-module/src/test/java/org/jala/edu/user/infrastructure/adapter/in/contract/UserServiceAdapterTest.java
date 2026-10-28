package org.jala.edu.user.infrastructure.adapter.in.contract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jala.edu.user.UserFixtures.user;

import java.util.UUID;
import org.jala.edu.core.contract.UserRole;
import org.jala.edu.user.application.usecase.CheckUserAccessUseCase;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.infrastructure.adapter.out.persistence.memory.InMemoryUserRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class UserServiceAdapterTest {

    private final InMemoryUserRepositoryAdapter repository = new InMemoryUserRepositoryAdapter();
    private final UserServiceAdapter userService =
            new UserServiceAdapter(new CheckUserAccessUseCase(repository));

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void everyContractRoleMatchesTheUserRoleWithTheSameName(UserRole contractRole) {
        // Arrange
        User user = user("ana@example.com");
        user.changeRole(org.jala.edu.user.domain.model.UserRole.valueOf(contractRole.name()));
        repository.save(user);

        // Act
        boolean allowed = userService.isActiveWithRole(user.getId().value(), contractRole);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void rejectsWhenNoRoleIsRequested() {
        // Arrange
        UUID customerId = repository.save(user("ana@example.com")).getId().value();

        // Act
        boolean allowed = userService.isActiveWithRole(customerId);

        // Assert
        assertThat(allowed).isFalse();
    }
}
