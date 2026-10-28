package org.jala.edu.user.infrastructure.config;

import org.jala.edu.core.contract.UserService;
import org.jala.edu.user.application.port.out.UserRepositoryPort;
import org.jala.edu.user.application.usecase.CheckUserAccessUseCase;
import org.jala.edu.user.application.usecase.CreateUserUseCase;
import org.jala.edu.user.application.usecase.DeleteUserUseCase;
import org.jala.edu.user.application.usecase.ListUsersUseCase;
import org.jala.edu.user.application.usecase.UpdateUserUseCase;
import org.jala.edu.user.infrastructure.adapter.in.contract.UserServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserModuleConfiguration {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new CreateUserUseCase(userRepositoryPort);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new UpdateUserUseCase(userRepositoryPort);
    }

    @Bean
    public ListUsersUseCase listUsersUseCase(UserRepositoryPort userRepositoryPort) {
        return new ListUsersUseCase(userRepositoryPort);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new DeleteUserUseCase(userRepositoryPort);
    }

    @Bean
    public CheckUserAccessUseCase checkUserAccessUseCase(UserRepositoryPort userRepositoryPort) {
        return new CheckUserAccessUseCase(userRepositoryPort);
    }

    /**
     * The {@link UserService} contract other modules receive; it replaces core's fallback.
     */
    @Bean
    public UserService userService(CheckUserAccessUseCase checkUserAccessUseCase) {
        return new UserServiceAdapter(checkUserAccessUseCase);
    }

    @Bean
    public SampleUsersLoader sampleUsersLoader(UserRepositoryPort userRepositoryPort) {
        return new SampleUsersLoader(userRepositoryPort);
    }
}
