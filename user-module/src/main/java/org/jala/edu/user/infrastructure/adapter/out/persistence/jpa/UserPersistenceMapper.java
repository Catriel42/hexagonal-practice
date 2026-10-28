package org.jala.edu.user.infrastructure.adapter.out.persistence.jpa;

import org.jala.edu.core.infrastructure.persistence.jpa.PersistenceMapper;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;

final class UserPersistenceMapper implements PersistenceMapper<User, UserId, UserJpaEntity, String> {

    @Override
    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                toEntityId(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().value(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    @Override
    public User toDomain(UserJpaEntity entity) {
        return User.restore(
                UserId.of(entity.getId()),
                entity.getFirstName(),
                entity.getLastName(),
                new Email(entity.getEmail()),
                entity.getPhoneNumber(),
                UserRole.valueOf(entity.getRole()),
                UserStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    @Override
    public String toEntityId(UserId id) {
        return id.toString();
    }
}
