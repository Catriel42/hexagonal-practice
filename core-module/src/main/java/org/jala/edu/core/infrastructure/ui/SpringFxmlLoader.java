package org.jala.edu.core.infrastructure.ui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Loads FXML views whose controllers are Spring beans, so they get their dependencies injected.
 * Controllers should be prototype-scoped: each loaded view needs its own controller instance.
 */
@Component
@RequiredArgsConstructor
public class SpringFxmlLoader {

    private final ApplicationContext applicationContext;

    public Parent load(String fxmlPath) {
        return loadView(fxmlPath).root();
    }

    /**
     * Loads a view and also returns its controller, e.g. to pass it data before showing the view.
     */
    public <C> View<C> loadView(String fxmlPath) {
        URL resource = getClass().getResource(fxmlPath);
        if (resource == null) {
            throw new IllegalArgumentException("View not found on classpath: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        try {
            Parent root = loader.load();
            return new View<>(root, loader.getController());
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load view " + fxmlPath, e);
        }
    }

    public record View<C>(Parent root, C controller) {
    }
}
