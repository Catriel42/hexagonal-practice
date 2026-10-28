package org.jala.edu.user.infrastructure.adapter.in.ui;

import static org.jala.edu.core.infrastructure.ui.FormBindings.isBlank;
import static org.jala.edu.core.infrastructure.ui.FormBindings.optionalText;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.RequiredArgsConstructor;
import org.jala.edu.core.infrastructure.ui.BackgroundTasks;
import org.jala.edu.core.infrastructure.ui.EnumLabelConverter;
import org.jala.edu.core.infrastructure.ui.ErrorMessages;
import org.jala.edu.user.application.port.in.CreateUserCommand;
import org.jala.edu.user.application.usecase.CreateUserUseCase;
import org.jala.edu.user.domain.model.UserRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * JavaFX inbound adapter for the "create user" use case (view: {@code create-user.fxml}).
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    @FXML
    private GridPane form;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<UserRole> roleComboBox;

    @FXML
    private Button createButton;

    @FXML
    private Label messageLabel;

    @FXML
    void initialize() {
        roleComboBox.getItems().setAll(UserRole.values());
        roleComboBox.setConverter(new EnumLabelConverter<>(UserRole.class));
        createButton.disableProperty().bind(isBlank(firstNameField)
                .or(isBlank(lastNameField))
                .or(isBlank(emailField))
                .or(roleComboBox.valueProperty().isNull()));
    }

    @FXML
    void onCreate() {
        CreateUserCommand command = new CreateUserCommand(
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                optionalText(phoneField),
                roleComboBox.getValue());

        form.setDisable(true);
        messageLabel.setText("");
        BackgroundTasks.run(
                () -> createUserUseCase.execute(command),
                userId -> {
                    clearForm();
                    showMessage("User " + command.firstName() + " " + command.lastName()    + " created (" + command.email() + ").", "success");
                    form.setDisable(false);
                },
                error -> {
                    showMessage(ErrorMessages.of(error, "create the user"), "error");
                    form.setDisable(false);
                });
    }

    private void showMessage(String text, String styleClass) {
        messageLabel.getStyleClass().removeAll("success", "error");
        messageLabel.getStyleClass().add(styleClass);
        messageLabel.setText(text);
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        firstNameField.requestFocus();
    }
}
