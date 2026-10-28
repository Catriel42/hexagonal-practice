package org.jala.edu.core.infrastructure.ui;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jala.edu.core.domain.exception.DomainException;

/**
 * Turns a failure into a message for the user: business rule violations are shown as they are,
 * anything else is logged and replaced by a generic message.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {

    /**
     * @param action what was being attempted, e.g. {@code "update the user"}
     */
    public static String of(Throwable error, String action) {
        if (error instanceof DomainException) {
            return error.getMessage();
        }
        log.error("Could not {}", action, error);
        return "Unexpected error: could not " + action + ". Please try again.";
    }
}
