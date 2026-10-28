package org.jala.edu.core.contract;

import java.util.UUID;

/**
 * Identity check offered by {@code user-module} to every other module.
 */
public interface UserService {

    /**
     * @return {@code true} only if the user exists, is active and has at least one of the given roles
     */
    boolean isActiveWithRole(UUID userId, UserRole... roles);
}
