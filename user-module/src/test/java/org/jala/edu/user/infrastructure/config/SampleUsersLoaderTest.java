package org.jala.edu.user.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.infrastructure.adapter.out.persistence.memory.InMemoryUserRepositoryAdapter;
import org.junit.jupiter.api.Test;

class SampleUsersLoaderTest {

    private final InMemoryUserRepositoryAdapter repository = new InMemoryUserRepositoryAdapter();
    private final SampleUsersLoader loader = new SampleUsersLoader(repository);

    @Test
    void storesOneActiveUserPerRoleAndAnInactiveCustomer() {
        // Arrange (empty repository)

        // Act
        loader.load();

        // Assert
        assertThat(repository.findAll()).hasSize(5);
        assertThat(stored(SampleUsers.MERCHANT).getRole()).isEqualTo(UserRole.MERCHANT);
        assertThat(stored(SampleUsers.MERCHANT).isActive()).isTrue();
        assertThat(stored(SampleUsers.INACTIVE_CUSTOMER).isActive()).isFalse();
    }

    @Test
    void loadingAgainKeepsASingleCopyOfEachUser() {
        // Arrange
        loader.load();

        // Act
        loader.load();

        // Assert
        assertThat(repository.findAll()).hasSize(5);
    }

    private User stored(UUID id) {
        return repository.findById(new UserId(id)).orElseThrow();
    }
}
