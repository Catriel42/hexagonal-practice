package org.jala.edu.catalog.infrastructure.config;

import org.jala.edu.core.infrastructure.ui.NavigationItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Adds the "Catalog" button to the main window navigation bar; it opens the module's main view.
 */
@Configuration
public class CatalogUiConfiguration {

    public static final String MAIN_VIEW = "/org/jala/edu/catalog/infrastructure/adapter/in/ui/catalog-main.fxml";

    @Bean
    @Order(200)
    public NavigationItem catalogNavigationItem() {
        return new NavigationItem("Catalog", MAIN_VIEW);
    }
}
