package org.jala.edu.user.application.usecase;

import org.jala.edu.core.application.usecase.UseCase;
import org.jala.edu.user.application.port.in.UpdateUserCommand;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.exception.UserNotFoundException;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;
import org.jala.edu.user.domain.model.UserStatus;

public class UpdateUserUseCase implements UseCase<UpdateUserCommand, Void> {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @throws UserNotFoundException if no user has the command's id
     */
    @Override
    public Void execute(UpdateUserCommand command) {
        UserId id = new UserId(command.id());
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        applyChanges(user, command);
        userRepository.save(user);
        return null;
    }

    private void applyChanges(User user, UpdateUserCommand command) {
        user.updateProfile(command.firstName(), command.lastName(), command.phoneNumber());
        if (command.role() != user.getRole()) {
            user.changeRole(command.role());
        }
        if (command.status() != user.getStatus()) {
            if (command.status() == UserStatus.ACTIVE) {
                user.activate();
            } else {
                user.deactivate();
            }
        }
    }
}
