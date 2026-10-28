package org.jala.edu.user;

import java.time.Instant;
import org.jala.edu.user.application.port.in.CreateUserCommand;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;

/**
 * Test data for the Arrange step. Builds valid users and commands with sensible defaults,
 * so each test only spells out the values that matter to it.
 */
public final class UserFixtures {

    public static final String FIRST_NAME = "Ana";
    public static final String LAST_NAME = "Perez";
    public static final String PHONE = "70000000";

    private UserFixtures() {
    }

    /**
     * A new active customer, created now.
     */
    public static User user(String email) {
        return User.register(FIRST_NAME, LAST_NAME, new Email(email), PHONE, UserRole.CUSTOMER);
    }

    /**
     * An active customer created (and last updated) at the given instant, e.g. to test ordering deterministically.
     */
    public static User userCreatedAt(String email, String createdAt) {
        Instant instant = Instant.parse(createdAt);
        return User.restore(UserId.newId(), FIRST_NAME, LAST_NAME, new Email(email), PHONE,
                UserRole.CUSTOMER, UserStatus.ACTIVE, instant, instant);
    }

    public static CreateUserCommand createCommand(String email) {
        return new CreateUserCommand(FIRST_NAME, LAST_NAME, email, PHONE, UserRole.CUSTOMER);
    }
}
