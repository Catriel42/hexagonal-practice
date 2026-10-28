package org.jala.edu.user.application.usecase;

import java.util.UUID;
import org.jala.edu.core.application.usecase.UseCase;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.exception.UserNotFoundException;
import org.jala.edu.user.domain.model.UserId;

public class DeleteUserUseCase implements UseCase<UUID, Void> {

    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @throws UserNotFoundException if no user has the given id
     */
    @Override
    public Void execute(UUID userId) {
        UserId id = new UserId(userId);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return null;
    }
}
