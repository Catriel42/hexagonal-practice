package org.jala.edu.user.application.usecase;

import java.util.List;
import org.jala.edu.core.application.usecase.UseCase;
import org.jala.edu.user.application.port.in.ListUsersQuery;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.domain.model.User;

public class ListUsersUseCase implements UseCase<ListUsersQuery, List<User>> {

    private final UserRepositoryPort userRepository;

    public ListUsersUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns every user, oldest first.
     */
    @Override
    public List<User> execute(ListUsersQuery query) {
        return userRepository.findAll();
    }
}
