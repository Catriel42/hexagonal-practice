package org.jala.edu.core.infrastructure.ui;

import java.util.Arrays;
import javafx.util.StringConverter;

/**
 * Shows enum constants with {@link DisplayFormat#label(Enum)} in combo boxes and similar controls.
 */
public class EnumLabelConverter<E extends Enum<E>> extends StringConverter<E> {

    private final Class<E> type;

    public EnumLabelConverter(Class<E> type) {
        this.type = type;
    }

    @Override
    public String toString(E value) {
        return DisplayFormat.label(value);
    }

    @Override
    public E fromString(String text) {
        return Arrays.stream(type.getEnumConstants())
                .filter(value -> DisplayFormat.label(value).equals(text))
                .findFirst()
                .orElse(null);
    }
}
