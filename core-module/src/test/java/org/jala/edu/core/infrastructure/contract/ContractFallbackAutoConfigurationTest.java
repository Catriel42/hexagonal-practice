package org.jala.edu.core.infrastructure.contract;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.jala.edu.core.contract.CatalogService;
import org.jala.edu.core.contract.OrderService;
import org.jala.edu.core.contract.PaymentService;
import org.jala.edu.core.contract.UserRole;
import org.jala.edu.core.contract.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class ContractFallbackAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ContractFallbackAutoConfiguration.class));

    @Test
    void registersAFallbackForEveryContractNoModuleProvides() {
        // Arrange (no module configuration)

        // Act (start the context) + Assert (inside the callback, while the context is running)
        runner.run(context -> {
            assertThat(context).getBean(UserService.class).isInstanceOf(FallbackUserService.class);
            assertThat(context).getBean(CatalogService.class).isInstanceOf(FallbackCatalogService.class);
            assertThat(context).getBean(PaymentService.class).isInstanceOf(FallbackPaymentService.class);
            assertThat(context).getBean(OrderService.class).isInstanceOf(FallbackOrderService.class);
        });
    }

    @Test
    void aModuleServiceReplacesTheFallback() {
        // Arrange
        ApplicationContextRunner withUserModule = runner.withUserConfiguration(UserModuleStub.class);

        // Act (start the context) + Assert (inside the callback, while the context is running)
        withUserModule.run(context -> {
            assertThat(context).hasSingleBean(UserService.class);
            assertThat(context).getBean(UserService.class).isNotInstanceOf(FallbackUserService.class);
            assertThat(context).getBean(CatalogService.class).isInstanceOf(FallbackCatalogService.class);
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class UserModuleStub {

        @Bean
        UserService userService() {
            return (UUID userId, UserRole... roles) -> true;
        }
    }
}
