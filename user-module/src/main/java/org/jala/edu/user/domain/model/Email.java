package org.jala.edu.user.domain.model;

import java.util.Locale;
import java.util.regex.Pattern;
import org.jala.edu.core.domain.exception.DomainException;

public record Email(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null || !PATTERN.matcher(value.strip()).matches()) {
            throw new DomainException("Invalid email address: " + value);
        }
        value = value.strip().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return value;
    }
}
