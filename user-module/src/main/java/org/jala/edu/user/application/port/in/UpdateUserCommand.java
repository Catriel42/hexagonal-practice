package org.jala.edu.user.application.port.in;

import java.util.UUID;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;

/**
 * Editable user data for the user with the given id. The email is the user's identity for login and cannot be changed.
 */
public record UpdateUserCommand(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        UserRole role,
        UserStatus status) {
}
