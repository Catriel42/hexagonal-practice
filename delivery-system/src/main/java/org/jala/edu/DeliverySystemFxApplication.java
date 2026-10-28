package org.jala.edu;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jala.edu.core.infrastructure.ui.StageReadyEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * JavaFX entry point: starts the Spring context before the window opens and closes it when the window closes.
 */
public class DeliverySystemFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(DeliverySystemApplication.class)
                .headless(false)
                .run(getParameters().getRaw().toArray(String[]::new));
    }

    @Override
    public void start(Stage stage) {
        context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        context.close();
    }
}
