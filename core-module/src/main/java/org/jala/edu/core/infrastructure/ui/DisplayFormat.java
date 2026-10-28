package org.jala.edu.core.infrastructure.ui;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Consistent formatting of domain values for display in views.
 */
public final class DisplayFormat {

    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private DisplayFormat() {
    }

    /**
     * {@code COURIER} → {@code "Courier"}; {@code null} → {@code ""}.
     */
    public static String label(Enum<?> value) {
        if (value == null) {
            return "";
        }
        String name = value.name().replace('_', ' ').toLowerCase(Locale.ROOT);
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Formats an instant in the user's local time zone; {@code null} → {@code ""}.
     */
    public static String dateTime(Instant value) {
        return value == null ? "" : DATE_TIME.format(value);
    }
}
