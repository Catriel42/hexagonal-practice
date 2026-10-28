package org.jala.edu.core.infrastructure.ui;

/**
 * A module's entry in the main window navigation bar: a button labelled {@code title} that opens the module's
 * main view ({@code fxmlPath}, a classpath resource). Modules expose one as a Spring bean; buttons appear in
 * {@link org.springframework.core.annotation.Order @Order} order.
 */
public record NavigationItem(String title, String fxmlPath) {
}
