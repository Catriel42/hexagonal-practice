package org.jala.edu.core.infrastructure.contract;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.core.contract.UserRole;
import org.junit.jupiter.api.Test;

class FallbackUserServiceTest {

    private final FallbackUserService userService = new FallbackUserService();

    @Test
    void acceptsASampleUserWithOneOfTheRequestedRoles() {
        // Arrange
        UUID merchant = SampleUsers.MERCHANT;

        // Act
        boolean allowed = userService.isActiveWithRole(merchant, UserRole.ADMIN, UserRole.MERCHANT);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void rejectsASampleUserWithAnotherRole() {
        // Arrange
        UUID courier = SampleUsers.COURIER;

        // Act
        boolean allowed = userService.isActiveWithRole(courier, UserRole.CUSTOMER);

        // Assert
        assertThat(allowed).isFalse();
    }

    @Test
    void rejectsTheInactiveSampleUserAndUnknownIds() {
        // Arrange
        UUID inactive = SampleUsers.INACTIVE_CUSTOMER;
        UUID unknown = UUID.randomUUID();

        // Act
        boolean inactiveAllowed = userService.isActiveWithRole(inactive, UserRole.CUSTOMER);
        boolean unknownAllowed = userService.isActiveWithRole(unknown, UserRole.CUSTOMER);

        // Assert
        assertThat(inactiveAllowed).isFalse();
        assertThat(unknownAllowed).isFalse();
    }
}
