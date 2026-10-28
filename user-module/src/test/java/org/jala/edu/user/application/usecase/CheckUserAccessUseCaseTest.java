package org.jala.edu.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jala.edu.user.UserFixtures.user;

import java.util.Set;
import java.util.UUID;
import org.jala.edu.user.application.port.in.CheckUserAccessQuery;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.infrastructure.adapter.out.persistence.memory.InMemoryUserRepositoryAdapter;
import org.junit.jupiter.api.Test;

class CheckUserAccessUseCaseTest {

    private final InMemoryUserRepositoryAdapter repository = new InMemoryUserRepositoryAdapter();
    private final CheckUserAccessUseCase checkUserAccess = new CheckUserAccessUseCase(repository);

    @Test
    void allowsAnActiveUserWithOneOfTheRoles() {
        // Arrange
        User merchant = user("ana@example.com");
        merchant.changeRole(UserRole.MERCHANT);
        repository.save(merchant);
        CheckUserAccessQuery query = new CheckUserAccessQuery(merchant.getId().value(),
                Set.of(UserRole.ADMIN, UserRole.MERCHANT));

        // Act
        boolean allowed = checkUserAccess.execute(query);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void rejectsAUserWithoutAnyOfTheRoles() {
        // Arrange
        User customer = repository.save(user("ana@example.com"));
        CheckUserAccessQuery query = new CheckUserAccessQuery(customer.getId().value(), Set.of(UserRole.COURIER));

        // Act
        boolean allowed = checkUserAccess.execute(query);

        // Assert
        assertThat(allowed).isFalse();
    }

    @Test
    void rejectsAnInactiveUserEvenWithTheRole() {
        // Arrange
        User customer = user("ana@example.com");
        customer.deactivate();
        repository.save(customer);
        CheckUserAccessQuery query = new CheckUserAccessQuery(customer.getId().value(), Set.of(UserRole.CUSTOMER));

        // Act
        boolean allowed = checkUserAccess.execute(query);

        // Assert
        assertThat(allowed).isFalse();
    }

    @Test
    void rejectsAnUnknownUserWithoutThrowing() {
        // Arrange
        CheckUserAccessQuery query = new CheckUserAccessQuery(UUID.randomUUID(), Set.of(UserRole.CUSTOMER));

        // Act
        boolean allowed = checkUserAccess.execute(query);

        // Assert
        assertThat(allowed).isFalse();
    }
}
