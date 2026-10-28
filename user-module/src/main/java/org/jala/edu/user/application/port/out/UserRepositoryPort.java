package org.jala.edu.user.application.port.out;

import org.jala.edu.core.application.port.out.CrudRepositoryPort;
import org.jala.edu.user.domain.model.Email;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserId;

public interface UserRepositoryPort extends CrudRepositoryPort<User, UserId> {

    boolean existsByEmail(Email email);
}
