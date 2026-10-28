package org.jala.edu.user.infrastructure.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jala.edu.user.UserFixtures.user;
import static org.jala.edu.user.UserFixtures.userCreatedAt;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

/**
 * Runs the JPA adapter (and the generic base it extends) against H2 in MySQL mode,
 * with the schema created by the module's real Flyway migrations.
 */
@DataJpaTest(properties = "spring.datasource.url=jdbc:h2:mem:users;MODE=MySQL;DATABASE_TO_LOWER=TRUE")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserPersistenceAdapter.class)
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter adapter;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdReadsBackEveryField() {
        // Arrange
        User saved = adapter.save(user("ana@example.com"));
        detach();

        // Act
        Optional<User> found = adapter.findById(saved.getId());

        // Assert
        assertThat(found).hasValueSatisfying(loaded -> {
            assertThat(loaded.getId()).isEqualTo(saved.getId());
            assertThat(loaded.fullName()).isEqualTo("Ana Perez");
            assertThat(loaded.getEmail().value()).isEqualTo("ana@example.com");
            assertThat(loaded.getPhoneNumber()).isEqualTo("70000000");
            assertThat(loaded.getRole()).isEqualTo(UserRole.CUSTOMER);
            assertThat(loaded.getStatus()).isEqualTo(UserStatus.ACTIVE);
            assertThat(loaded.getCreatedAt()).isEqualTo(saved.getCreatedAt());
            assertThat(loaded.getUpdatedAt()).isEqualTo(saved.getUpdatedAt());
        });
    }

    @Test
    void saveUpdatesTheExistingRow() {
        // Arrange
        User user = adapter.save(user("ana@example.com"));
        detach();
        user.updateProfile("Ana Maria", "Lopez", null);
        user.deactivate();

        // Act
        adapter.save(user);
        detach();

        // Assert
        User loaded = adapter.findById(user.getId()).orElseThrow();
        assertThat(loaded.fullName()).isEqualTo("Ana Maria Lopez");
        assertThat(loaded.getPhoneNumber()).isNull();
        assertThat(loaded.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(loaded.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(adapter.findAll()).hasSize(1);
    }

    @Test
    void findAllReturnsUsersOldestFirst() {
        // Arrange
        User newer = adapter.save(userCreatedAt("luis@example.com", "2026-02-01T10:00:00Z"));
        User older = adapter.save(userCreatedAt("ana@example.com", "2026-01-01T10:00:00Z"));
        detach();

        // Act
        List<User> users = adapter.findAll();

        // Assert
        assertThat(users).extracting(User::getId).containsExactly(older.getId(), newer.getId());
    }

    @Test
    void existsByEmailIgnoresCase() {
        // Arrange
        adapter.save(user("ana@example.com"));
        detach();

        // Act
        boolean exists = adapter.existsByEmail(new Email("ANA@example.com"));

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmailIsFalseForAnUnknownEmail() {
        // Arrange
        adapter.save(user("ana@example.com"));
        detach();

        // Act
        boolean exists = adapter.existsByEmail(new Email("other@example.com"));

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void deleteByIdRemovesTheRow() {
        // Arrange
        User user = adapter.save(user("ana@example.com"));
        detach();

        // Act
        adapter.deleteById(user.getId());
        detach();

        // Assert
        assertThat(adapter.existsById(user.getId())).isFalse();
        assertThat(adapter.findById(user.getId())).isEmpty();
    }

    /**
     * Writes pending changes and empties the persistence context so the next read hits the database.
     */
    private void detach() {
        entityManager.flush();
        entityManager.clear();
    }
}
