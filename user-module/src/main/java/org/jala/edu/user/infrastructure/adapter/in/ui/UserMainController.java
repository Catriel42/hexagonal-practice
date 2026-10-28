package org.jala.edu.user.infrastructure.adapter.in.ui;

import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import org.jala.edu.core.infrastructure.ui.Navigator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Main view of the user module (view: {@code user-main.fxml}): the module's menu of screens.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserMainController {

    private final Navigator navigator;

    @FXML
    void onCreateUser() {
        navigator.show(UserViews.CREATE);
    }

    @FXML
    void onListUsers() {
        navigator.show(UserViews.LIST);
    }
}
