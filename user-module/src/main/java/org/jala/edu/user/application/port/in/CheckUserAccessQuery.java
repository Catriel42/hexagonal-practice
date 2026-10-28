package org.jala.edu.user.application.port.in;

import java.util.Set;
import java.util.UUID;
import org.jala.edu.user.domain.model.UserRole;

/**
 * Input of the "check user access" use case: is this user active and does it have any of these roles?
 */
public record CheckUserAccessQuery(UUID userId, Set<UserRole> roles) {
}
