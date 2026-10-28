package org.jala.edu.user.application.usecase;

import org.jala.edu.core.application.usecase.UseCase;
import org.jala.edu.user.application.port.in.CheckUserAccessQuery;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;

/**
 * Answers the identity check other modules make before an operation. An unknown user is not an error here:
 * the answer is simply {@code false}, and the calling module decides how to reject the operation.
 */
public class CheckUserAccessUseCase implements UseCase<CheckUserAccessQuery, Boolean> {

    private final UserRepositoryPort userRepository;

    public CheckUserAccessUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean execute(CheckUserAccessQuery query) {
        return userRepository.findById(new UserId(query.userId()))
                .filter(User::isActive)
                .map(user -> query.roles().contains(user.getRole()))
                .orElse(false);
    }
}
