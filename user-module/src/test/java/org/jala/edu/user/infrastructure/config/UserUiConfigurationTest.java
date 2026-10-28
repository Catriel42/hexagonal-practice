package org.jala.edu.user.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import org.jala.edu.core.infrastructure.ui.NavigationItem;
import org.jala.edu.user.infrastructure.adapter.in.ui.UserViews;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserUiConfigurationTest {

    private final UserUiConfiguration configuration = new UserUiConfiguration();

    @Test
    void navigationButtonIsLabelledUsersAndOpensTheMainView() {
        // Arrange
        String expectedTitle = "Users";

        // Act
        NavigationItem item = configuration.userNavigationItem();

        // Assert
        assertThat(item.title()).isEqualTo(expectedTitle);
        assertThat(item.fxmlPath()).isEqualTo(UserViews.MAIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {UserViews.MAIN, UserViews.CREATE, UserViews.LIST, UserViews.EDIT})
    void everyUserViewExistsOnTheClasspath(String fxmlPath) {
        // Arrange
        Class<?> anyModuleClass = UserViews.class;

        // Act
        URL view = anyModuleClass.getResource(fxmlPath);

        // Assert
        assertThat(view).as("FXML on the classpath: %s", fxmlPath).isNotNull();
    }
}
