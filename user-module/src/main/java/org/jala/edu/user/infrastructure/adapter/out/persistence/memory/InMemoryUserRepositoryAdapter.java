package org.jala.edu.user.infrastructure.adapter.out.persistence.memory;

import org.jala.edu.core.infrastructure.config.ConditionalOnInMemoryPersistence;
import org.jala.edu.core.infrastructure.persistence.memory.InMemoryCrudRepository;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.springframework.stereotype.Component;

/**
 * Stores users in memory. Active when {@code delivery.persistence.type=memory};
 * data is lost when the application stops.
 */
@Component
@ConditionalOnInMemoryPersistence
public class InMemoryUserRepositoryAdapter extends InMemoryCrudRepository<User, UserId> implements UserRepositoryPort {

    @Override
    public boolean existsByEmail(Email email) {
        return stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
