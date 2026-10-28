package org.jala.edu.core.infrastructure.ui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

/**
 * Published once the JavaFX toolkit hands over the primary stage and the Spring context is ready.
 */
public class StageReadyEvent extends ApplicationEvent {

    public StageReadyEvent(Stage stage) {
        super(stage);
    }

    public Stage getStage() {
        return (Stage) getSource();
    }
}
