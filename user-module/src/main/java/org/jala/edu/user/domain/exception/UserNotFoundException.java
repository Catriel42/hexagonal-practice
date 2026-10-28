package org.jala.edu.user.domain.exception;

import org.jala.edu.core.domain.exception.NotFoundException;
import org.jala.edu.user.domain.model.UserId;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(UserId id) {
        super("User not found: " + id);
    }
}
