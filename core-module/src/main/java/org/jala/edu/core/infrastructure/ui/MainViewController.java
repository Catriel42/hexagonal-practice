package org.jala.edu.core.infrastructure.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main window shell: adds one navigation button per module ({@link NavigationItem}) and shows the module's
 * main view in the center when its button is pressed. The pressed button stays highlighted.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MainViewController {

    static final String ACTIVE = "active";

    private final ObjectProvider<NavigationItem> navigationItems;
    private final Navigator navigator;

    @FXML
    private BorderPane root;

    @FXML
    private HBox navigationBar;

    @FXML
    void initialize() {
        navigator.attach(root);
        navigationItems.orderedStream().forEach(item -> {
            Button button = new Button(item.title());
            button.getStyleClass().add("nav-button");
            button.setOnAction(event -> {
                navigator.show(item.fxmlPath());
                highlight(button);
            });
            navigationBar.getChildren().add(button);
        });
    }

    private void highlight(Button selected) {
        navigationBar.getChildren().forEach(node -> node.getStyleClass().remove(ACTIVE));
        selected.getStyleClass().add(ACTIVE);
    }
}
