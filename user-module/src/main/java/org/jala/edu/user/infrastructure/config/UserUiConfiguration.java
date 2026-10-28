package org.jala.edu.user.infrastructure.config;

import org.jala.edu.core.infrastructure.ui.NavigationItem;
import org.jala.edu.user.infrastructure.adapter.in.ui.UserViews;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Adds the "Users" button to the main window navigation bar; it opens the module's main view.
 */
@Configuration
public class UserUiConfiguration {

    @Bean
    @Order(100)
    public NavigationItem userNavigationItem() {
        return new NavigationItem("Users", UserViews.MAIN);
    }
}
