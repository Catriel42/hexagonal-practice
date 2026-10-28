package org.jala.edu.core.infrastructure.contract;

import lombok.extern.slf4j.Slf4j;
import org.jala.edu.core.contract.CatalogService;
import org.jala.edu.core.contract.OrderService;
import org.jala.edu.core.contract.PaymentService;
import org.jala.edu.core.contract.UserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Registers a stand-in for each integration contract that no module has implemented yet, so the application
 * starts while the owning team is still working. Auto-configuration runs after the modules' own configuration,
 * so a module that registers the real service always replaces the fallback.
 */
@Slf4j
@AutoConfiguration
public class ContractFallbackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserService fallbackUserService() {
        warnFallback(UserService.class);
        return new FallbackUserService();
    }

    @Bean
    @ConditionalOnMissingBean
    public CatalogService fallbackCatalogService() {
        warnFallback(CatalogService.class);
        return new FallbackCatalogService();
    }

    @Bean
    @ConditionalOnMissingBean
    public PaymentService fallbackPaymentService() {
        warnFallback(PaymentService.class);
        return new FallbackPaymentService();
    }

    @Bean
    @ConditionalOnMissingBean
    public OrderService fallbackOrderService() {
        warnFallback(OrderService.class);
        return new FallbackOrderService();
    }

    private static void warnFallback(Class<?> contract) {
        log.warn("No module provides {}; using the fallback with sample data", contract.getSimpleName());
    }
}
