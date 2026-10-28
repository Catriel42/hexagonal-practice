package org.jala.edu.core.infrastructure.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The main window builds its navigation buttons from {@code ObjectProvider<NavigationItem>.orderedStream()};
 * modules control where their buttons appear with {@code @Order} on their {@code @Bean} methods.
 */
class NavigationItemOrderTest {

    @Test
    void itemsFollowTheirOrderAnnotationNotTheRegistrationOrder() {
        // Arrange
        ApplicationContextRunner runner = new ApplicationContextRunner()
                .withUserConfiguration(SecondModuleNavigation.class, FirstModuleNavigation.class);

        // Act (start the context) + Assert (inside the callback, while the context is running)
        runner.run(context -> {
            List<String> buttons = context.getBeanProvider(NavigationItem.class).orderedStream()
                    .map(NavigationItem::title)
                    .toList();
            assertThat(buttons).containsExactly("First", "Second");
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class FirstModuleNavigation {

        @Bean
        @Order(100)
        NavigationItem firstItem() {
            return new NavigationItem("First", "/first.fxml");
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class SecondModuleNavigation {

        @Bean
        @Order(200)
        NavigationItem secondItem() {
            return new NavigationItem("Second", "/second.fxml");
        }
    }
}
