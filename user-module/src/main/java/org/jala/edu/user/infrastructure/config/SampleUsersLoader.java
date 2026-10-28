package org.jala.edu.user.infrastructure.config;

import java.time.Instant;
import java.util.UUID;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * Stores the {@link SampleUsers} at startup with their fixed ids, so other teams can test against the real
 * {@code UserService}. Users that already exist are left untouched.
 */
public class SampleUsersLoader implements ApplicationRunner {

    private final UserRepositoryPort userRepository;

    public SampleUsersLoader(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        load();
    }

    void load() {
        store(SampleUsers.ADMIN, "Admin", "admin@sample.delivery", UserRole.ADMIN, UserStatus.ACTIVE);
        store(SampleUsers.MERCHANT, "Merchant", "merchant@sample.delivery", UserRole.MERCHANT, UserStatus.ACTIVE);
        store(SampleUsers.CUSTOMER, "Customer", "customer@sample.delivery", UserRole.CUSTOMER, UserStatus.ACTIVE);
        store(SampleUsers.COURIER, "Courier", "courier@sample.delivery", UserRole.COURIER, UserStatus.ACTIVE);
        store(SampleUsers.INACTIVE_CUSTOMER, "Inactive", "inactive@sample.delivery", UserRole.CUSTOMER,
                UserStatus.INACTIVE);
    }

    private void store(UUID id, String lastName, String email, UserRole role, UserStatus status) {
        UserId userId = new UserId(id);
        Email userEmail = new Email(email);
        if (userRepository.existsById(userId) || userRepository.existsByEmail(userEmail)) {
            return;
        }
        Instant now = Instant.now();
        userRepository.save(User.restore(userId, "Sample", lastName, userEmail, null, role, status, now, now));
    }
}
