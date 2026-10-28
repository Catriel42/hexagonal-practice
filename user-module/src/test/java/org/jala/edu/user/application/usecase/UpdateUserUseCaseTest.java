package org.jala.edu.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.jala.edu.user.UserFixtures.user;

import java.util.UUID;
import org.jala.edu.core.domain.exception.DomainException;
import org.jala.edu.user.application.port.in.UpdateUserCommand;
import org.jala.edu.user.domain.exception.UserNotFoundException;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;
import org.jala.edu.user.infrastructure.adapter.out.persistence.memory.InMemoryUserRepositoryAdapter;
import org.junit.jupiter.api.Test;

class UpdateUserUseCaseTest {

    private final InMemoryUserRepositoryAdapter repository = new InMemoryUserRepositoryAdapter();
    private final UpdateUserUseCase updateUser = new UpdateUserUseCase(repository);

    @Test
    void updatesProfileRoleAndStatus() {
        // Arrange
        User existing = repository.save(user("ana@example.com"));
        UpdateUserCommand command = new UpdateUserCommand(existing.getId().value(), "Ana Maria", "Lopez", "71111111",
                UserRole.COURIER, UserStatus.INACTIVE);

        // Act
        updateUser.execute(command);

        // Assert
        User stored = repository.findById(existing.getId()).orElseThrow();
        assertThat(stored.fullName()).isEqualTo("Ana Maria Lopez");
        assertThat(stored.getPhoneNumber()).isEqualTo("71111111");
        assertThat(stored.getRole()).isEqualTo(UserRole.COURIER);
        assertThat(stored.getStatus()).isEqualTo(UserStatus.INACTIVE);
    }

    @Test
    void keepsTheEmail() {
        // Arrange
        User existing = repository.save(user("ana@example.com"));
        UpdateUserCommand command = new UpdateUserCommand(existing.getId().value(), "Ana Maria", "Lopez", null,
                UserRole.CUSTOMER, UserStatus.ACTIVE);

        // Act
        updateUser.execute(command);

        // Assert
        User stored = repository.findById(existing.getId()).orElseThrow();
        assertThat(stored.getEmail().value()).isEqualTo("ana@example.com");
    }

    @Test
    void rejectsBlankNamesAndLeavesTheUserUnchanged() {
        // Arrange
        User existing = repository.save(user("ana@example.com"));
        UpdateUserCommand invalid = new UpdateUserCommand(existing.getId().value(), "Maria", " ", null,
                UserRole.ADMIN, UserStatus.ACTIVE);

        // Act
        Throwable thrown = catchThrowable(() -> updateUser.execute(invalid));

        // Assert
        assertThat(thrown).isInstanceOf(DomainException.class).hasMessageContaining("Last name");
        User stored = repository.findById(existing.getId()).orElseThrow();
        assertThat(stored.fullName()).isEqualTo("Ana Perez");
        assertThat(stored.getRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    void throwsWhenTheUserDoesNotExist() {
        // Arrange
        UpdateUserCommand command = new UpdateUserCommand(UUID.randomUUID(), "Ana", "Perez", null,
                UserRole.CUSTOMER, UserStatus.ACTIVE);

        // Act
        Throwable thrown = catchThrowable(() -> updateUser.execute(command));

        // Assert
        assertThat(thrown).isInstanceOf(UserNotFoundException.class);
    }
}
