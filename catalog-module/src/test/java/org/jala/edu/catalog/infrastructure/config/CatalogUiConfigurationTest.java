package org.jala.edu.catalog.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import org.jala.edu.core.infrastructure.ui.NavigationItem;
import org.junit.jupiter.api.Test;

class CatalogUiConfigurationTest {

    private final CatalogUiConfiguration configuration = new CatalogUiConfiguration();

    @Test
    void navigationButtonIsLabelledCatalog() {
        // Arrange
        String expectedTitle = "Catalog";

        // Act
        NavigationItem item = configuration.catalogNavigationItem();

        // Assert
        assertThat(item.title()).isEqualTo(expectedTitle);
    }

    @Test
    void navigationButtonOpensAnExistingMainView() {
        // Arrange
        NavigationItem item = configuration.catalogNavigationItem();

        // Act
        URL view = getClass().getResource(item.fxmlPath());

        // Assert
        assertThat(item.fxmlPath()).isEqualTo(CatalogUiConfiguration.MAIN_VIEW);
        assertThat(view).as("FXML on the classpath: %s", item.fxmlPath()).isNotNull();
    }
}
