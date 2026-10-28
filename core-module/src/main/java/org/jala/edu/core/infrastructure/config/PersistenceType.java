package org.jala.edu.core.infrastructure.config;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Which outbound persistence adapters the application uses, selected with the
 * {@value #PROPERTY} property (system property, environment variable or application.yml).
 */
public enum PersistenceType {

    /** JPA adapters backed by MySQL, with Flyway migrations. The default. */
    JPA,

    /** In-memory adapters: no database needed, data is lost when the application stops. */
    MEMORY;

    public static final String PROPERTY = "delivery.persistence.type";

    /**
     * Parses a property value, ignoring case; {@code null} or blank means the default ({@link #JPA}).
     *
     * @throws IllegalArgumentException for unknown values, listing the accepted ones
     */
    public static PersistenceType from(String value) {
        if (value == null || value.isBlank()) {
            return JPA;
        }
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.strip()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value '" + value + "' for property '"
                        + PROPERTY + "'. Accepted values: " + acceptedValues()));
    }

    private static String acceptedValues() {
        return Arrays.stream(values())
                .map(type -> type.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(", "));
    }
}
