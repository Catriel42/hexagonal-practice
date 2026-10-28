package org.jala.edu.user.domain.exception;

import org.jala.edu.core.domain.exception.ConflictException;
import org.jala.edu.user.domain.model.Email;

public class EmailAlreadyRegisteredException extends ConflictException {

    public EmailAlreadyRegisteredException(Email email) {
        super("Email already registered: " + email);
    }
}
