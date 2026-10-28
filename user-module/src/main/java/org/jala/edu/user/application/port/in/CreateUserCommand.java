package org.jala.edu.user.application.port.in;

import org.jala.edu.user.domain.model.UserRole;

public record CreateUserCommand(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UserRole role) {
}
