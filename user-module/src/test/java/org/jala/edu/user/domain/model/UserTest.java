package org.jala.edu.user.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.jala.edu.user.UserFixtures.user;
import static org.jala.edu.user.UserFixtures.userCreatedAt;

import java.time.Instant;
import org.jala.edu.core.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

class UserTest {

    private static final String PAST = "2020-01-01T00:00:00Z";

    @Test
    void registersAnActiveUserWithNormalizedData() {
        // Arrange
        Email email = new Email(" Ana@Example.COM ");

        // Act
        User user = User.register("  Ana ", "Perez", email, "70000000", UserRole.CUSTOMER);

        // Assert
        assertThat(user.getId()).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("Ana");
        assertThat(user.getEmail().value()).isEqualTo("ana@example.com");
        assertThat(user.isActive()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void rejectsABlankFirstName() {
        // Arrange
        Email email = new Email("ana@example.com");

        // Act
        Throwable thrown = catchThrowable(() -> User.register(" ", "Perez", email, null, UserRole.CUSTOMER));

        // Assert
        assertThat(thrown).isInstanceOf(DomainException.class).hasMessageContaining("First name");
    }

    @Test
    void rejectsAnInvalidEmail() {
        // Arrange
        String invalid = "not-an-email";

        // Act
        Throwable thrown = catchThrowable(() -> new Email(invalid));

        // Assert
        assertThat(thrown).isInstanceOf(DomainException.class).hasMessageContaining(invalid);
    }

    @Test
    void deactivateMakesTheUserInactive() {
        // Arrange
        User user = user("ana@example.com");

        // Act
        user.deactivate();

        // Assert
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(user.isActive()).isFalse();
    }

    @Test
    void activateMakesAnInactiveUserActive() {
        // Arrange
        User user = user("ana@example.com");
        user.deactivate();

        // Act
        user.activate();

        // Assert
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void updateProfileRefreshesUpdatedAtButKeepsCreatedAt() {
        // Arrange
        User user = userCreatedAt("ana@example.com", PAST);

        // Act
        user.updateProfile("Ana Maria", "Perez", "70000000");

        // Assert
        assertThat(user.getCreatedAt()).isEqualTo(Instant.parse(PAST));
        assertThat(user.getUpdatedAt()).isAfter(Instant.parse(PAST));
    }

    @Test
    void rejectedProfileUpdateLeavesTheUserUnchanged() {
        // Arrange
        User user = userCreatedAt("ana@example.com", PAST);

        // Act
        Throwable thrown = catchThrowable(() -> user.updateProfile("Maria", " ", null));

        // Assert
        assertThat(thrown).isInstanceOf(DomainException.class).hasMessageContaining("Last name");
        assertThat(user.fullName()).isEqualTo("Ana Perez");
        assertThat(user.getUpdatedAt()).isEqualTo(Instant.parse(PAST));
    }

    @Test
    void usersWithTheSameIdAreEqual() {
        // Arrange
        User original = user("ana@example.com");
        User sameIdOtherData = User.restore(original.getId(), "Maria", "Lopez", new Email("maria@example.com"), null,
                UserRole.ADMIN, UserStatus.INACTIVE, original.getCreatedAt(), original.getUpdatedAt());

        // Act
        boolean equal = original.equals(sameIdOtherData);

        // Assert
        assertThat(equal).isTrue();
        assertThat(original).hasSameHashCodeAs(sameIdOtherData);
    }

    @Test
    void usersWithDifferentIdsAreNotEqualEvenWithTheSameData() {
        // Arrange
        User first = user("ana@example.com");
        User second = user("ana@example.com");

        // Act
        boolean equal = first.equals(second);

        // Assert
        assertThat(equal).isFalse();
    }
}
