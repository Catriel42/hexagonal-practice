package org.jala.edu.core.infrastructure.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextInputControl;

/**
 * Bindings for enabling form actions only when required fields are filled.
 */
public final class FormBindings {

    private FormBindings() {
    }

    public static BooleanBinding isBlank(TextInputControl field) {
        return Bindings.createBooleanBinding(() -> field.getText() == null || field.getText().isBlank(),
                field.textProperty());
    }

    /**
     * Returns the trimmed text, or {@code null} when the field is empty (for optional values).
     */
    public static String optionalText(TextInputControl field) {
        return field.getText() == null || field.getText().isBlank() ? null : field.getText().strip();
    }
}
