package org.jala.edu.core.infrastructure.ui;

import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Shows views in the content area of the main window. Module views use it to open their screens,
 * e.g. the Users main view opening "Create user".
 */
@Component
@RequiredArgsConstructor
public class Navigator {

    private final SpringFxmlLoader fxmlLoader;

    private BorderPane mainWindow;

    /**
     * Called by the main window once it is built; views are shown in its center.
     */
    void attach(BorderPane mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void show(String fxmlPath) {
        if (mainWindow == null) {
            throw new IllegalStateException("The main window is not initialized yet");
        }
        mainWindow.setCenter(fxmlLoader.load(fxmlPath));
    }
}
