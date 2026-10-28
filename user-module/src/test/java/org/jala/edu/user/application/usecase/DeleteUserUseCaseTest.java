package org.jala.edu.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.jala.edu.user.UserFixtures.user;

import java.util.UUID;
import org.jala.edu.user.domain.exception.UserNotFoundException;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.infrastructure.adapter.out.persistence.memory.InMemoryUserRepositoryAdapter;
import org.junit.jupiter.api.Test;

class DeleteUserUseCaseTest {

    private final InMemoryUserRepositoryAdapter repository = new InMemoryUserRepositoryAdapter();
    private final DeleteUserUseCase deleteUser = new DeleteUserUseCase(repository);

    @Test
    void deletesOnlyTheGivenUser() {
        // Arrange
        User toDelete = repository.save(user("ana@example.com"));
        User toKeep = repository.save(user("luis@example.com"));

        // Act
        deleteUser.execute(toDelete.getId().value());

        // Assert
        assertThat(repository.existsById(toDelete.getId())).isFalse();
        assertThat(repository.existsById(toKeep.getId())).isTrue();
    }

    @Test
    void throwsWhenTheUserDoesNotExist() {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        // Act
        Throwable thrown = catchThrowable(() -> deleteUser.execute(unknownId));

        // Assert
        assertThat(thrown).isInstanceOf(UserNotFoundException.class);
    }
}
