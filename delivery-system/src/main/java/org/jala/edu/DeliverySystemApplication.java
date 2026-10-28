package org.jala.edu;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the modular monolith. Lives in the root package {@code org.jala.edu} so component,
 * entity and repository scanning pick up every module ({@code org.jala.edu.core}, {@code org.jala.edu.user}, ...).
 * <p>
 * This class deliberately does not extend {@link Application}: that lets JavaFX run from the classpath
 * (and from the Spring Boot jar) without module-path flags.
 */
@SpringBootApplication
public class DeliverySystemApplication {

    public static void main(String[] args) {
        Application.launch(DeliverySystemFxApplication.class, args);
    }
}
