package org.jala.edu.user.infrastructure.adapter.in.ui;

import static org.jala.edu.core.infrastructure.ui.FormBindings.isBlank;
import static org.jala.edu.core.infrastructure.ui.FormBindings.optionalText;

import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.RequiredArgsConstructor;
import org.jala.edu.core.infrastructure.ui.BackgroundTasks;
import org.jala.edu.core.infrastructure.ui.Dialogs;
import org.jala.edu.core.infrastructure.ui.EnumLabelConverter;
import org.jala.edu.core.infrastructure.ui.ErrorMessages;
import org.jala.edu.user.application.port.in.UpdateUserCommand;
import org.jala.edu.user.application.usecase.UpdateUserUseCase;
import org.jala.edu.user.domain.model.User;
import org.jala.edu.user.domain.model.UserRole;
import org.jala.edu.user.domain.model.UserStatus;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * JavaFX inbound adapter for the "update user" use case (view: {@code edit-user.fxml}, shown as a dialog).
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class EditUserController {

    private final UpdateUserUseCase updateUserUseCase;

    private UUID userId;
    private Runnable onSaved;

    @FXML
    private GridPane form;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<UserRole> roleComboBox;

    @FXML
    private ComboBox<UserStatus> statusComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Label messageLabel;

    @FXML
    void initialize() {
        roleComboBox.getItems().setAll(UserRole.values());
        roleComboBox.setConverter(new EnumLabelConverter<>(UserRole.class));
        statusComboBox.getItems().setAll(UserStatus.values());
        statusComboBox.setConverter(new EnumLabelConverter<>(UserStatus.class));
        saveButton.disableProperty().bind(isBlank(firstNameField)
                .or(isBlank(lastNameField))
                .or(roleComboBox.valueProperty().isNull())
                .or(statusComboBox.valueProperty().isNull()));
    }

    /**
     * Fills the form with {@code user}; {@code onSaved} runs after a successful save.
     */
    public void edit(User user, Runnable onSaved) {
        this.userId = user.getId().value();
        this.onSaved = onSaved;
        emailLabel.setText(user.getEmail().value());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        phoneField.setText(user.getPhoneNumber());
        roleComboBox.setValue(user.getRole());
        statusComboBox.setValue(user.getStatus());
    }

    @FXML
    void onSave() {
        UpdateUserCommand command = new UpdateUserCommand(
                userId,
                firstNameField.getText(),
                lastNameField.getText(),
                optionalText(phoneField),
                roleComboBox.getValue(),
                statusComboBox.getValue());

        form.setDisable(true);
        messageLabel.setText("");
        BackgroundTasks.run(
                () -> updateUserUseCase.execute(command),
                done -> {
                    Dialogs.close(form);
                    onSaved.run();
                },
                error -> {
                    messageLabel.setText(ErrorMessages.of(error, "update the user"));
                    form.setDisable(false);
                });
    }

    @FXML
    void onCancel() {
        Dialogs.close(form);
    }
}
