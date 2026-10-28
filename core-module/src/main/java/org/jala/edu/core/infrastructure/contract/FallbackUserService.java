package org.jala.edu.core.infrastructure.contract;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.core.contract.UserRole;
import org.jala.edu.core.contract.UserService;

/**
 * Stand-in {@link UserService} that only knows the {@link SampleUsers}. Teams can also use it in their unit tests.
 */
public class FallbackUserService implements UserService {

    private static final Map<UUID, UserRole> ACTIVE_USERS = Map.of(
            SampleUsers.ADMIN, UserRole.ADMIN,
            SampleUsers.MERCHANT, UserRole.MERCHANT,
            SampleUsers.CUSTOMER, UserRole.CUSTOMER,
            SampleUsers.COURIER, UserRole.COURIER);

    @Override
    public boolean isActiveWithRole(UUID userId, UserRole... roles) {
        UserRole role = ACTIVE_USERS.get(userId);
        return role != null && Arrays.asList(roles).contains(role);
    }
}
