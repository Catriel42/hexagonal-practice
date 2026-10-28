package org.jala.edu.core.domain.exception;

/**
 * Base type for business rule violations raised by any module's domain or application layer.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
