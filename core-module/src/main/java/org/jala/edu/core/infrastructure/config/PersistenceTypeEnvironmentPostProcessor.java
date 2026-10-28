package org.jala.edu.core.infrastructure.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Validates {@value PersistenceType#PROPERTY} at startup and, for {@link PersistenceType#MEMORY}, switches off the
 * database auto-configuration so the application starts without a DataSource. Runs after application.yml is loaded,
 * so the property can come from there, a system property or an environment variable.
 * Registered in {@code META-INF/spring.factories}.
 */
public class PersistenceTypeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    static final String EXCLUDE_PROPERTY = "spring.autoconfigure.exclude";
    static final String PROPERTY_SOURCE_NAME = "inMemoryPersistence";

    static final List<String> DATABASE_AUTO_CONFIGURATIONS = List.of(
            "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
            "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
            "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration",
            "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration");

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PersistenceType type = PersistenceType.from(environment.getProperty(PersistenceType.PROPERTY));
        if (type != PersistenceType.MEMORY) {
            return;
        }
        Set<String> excludes = new LinkedHashSet<>(Binder.get(environment)
                .bind(EXCLUDE_PROPERTY, Bindable.listOf(String.class))
                .orElse(List.of()));
        excludes.addAll(DATABASE_AUTO_CONFIGURATIONS);
        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME,
                Map.of(EXCLUDE_PROPERTY, String.join(",", excludes))));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
