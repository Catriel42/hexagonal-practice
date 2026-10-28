package org.jala.edu.core.infrastructure.ui;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private static final String MAIN_VIEW = "/org/jala/edu/core/infrastructure/ui/main-view.fxml";
    private static final String STYLESHEET = "/org/jala/edu/core/infrastructure/ui/app.css";

    private final SpringFxmlLoader fxmlLoader;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Scene scene = new Scene(fxmlLoader.load(MAIN_VIEW), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());

        Stage stage = event.getStage();
        stage.setTitle("Delivery System");
        stage.setScene(scene);
        stage.show();
    }
}
