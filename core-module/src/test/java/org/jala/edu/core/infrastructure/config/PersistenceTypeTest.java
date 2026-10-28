package org.jala.edu.core.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.env.MockEnvironment;

class PersistenceTypeTest {

    private static final String EXCLUDE = PersistenceTypeEnvironmentPostProcessor.EXCLUDE_PROPERTY;

    private final PersistenceTypeEnvironmentPostProcessor postProcessor = new PersistenceTypeEnvironmentPostProcessor();

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AdaptersConfiguration.class);

    @Test
    void withoutThePropertyTheAutoConfigurationIsLeftAlone() {
        // Arrange
        MockEnvironment environment = new MockEnvironment();

        // Act
        postProcessor.postProcessEnvironment(environment, new SpringApplication());

        // Assert
        assertThat(environment.getPropertySources()
                .contains(PersistenceTypeEnvironmentPostProcessor.PROPERTY_SOURCE_NAME)).isFalse();
        assertThat(environment.getProperty(EXCLUDE)).isNull();
    }

    @Test
    void memoryExcludesTheDatabaseAutoConfigurationIgnoringCase() {
        // Arrange
        MockEnvironment environment = new MockEnvironment().withProperty(PersistenceType.PROPERTY, "MEMORY");

        // Act
        postProcessor.postProcessEnvironment(environment, new SpringApplication());

        // Assert
        assertThat(environment.getProperty(EXCLUDE))
                .contains(PersistenceTypeEnvironmentPostProcessor.DATABASE_AUTO_CONFIGURATIONS);
    }

    @Test
    void memoryKeepsExistingExcludes() {
        // Arrange
        MockEnvironment environment = new MockEnvironment()
                .withProperty(PersistenceType.PROPERTY, "memory")
                .withProperty(EXCLUDE, "com.example.OtherAutoConfiguration");

        // Act
        postProcessor.postProcessEnvironment(environment, new SpringApplication());

        // Assert
        assertThat(environment.getProperty(EXCLUDE))
                .startsWith("com.example.OtherAutoConfiguration,")
                .contains(PersistenceTypeEnvironmentPostProcessor.DATABASE_AUTO_CONFIGURATIONS);
    }

    @Test
    void anUnknownValueIsRejectedWithTheAcceptedOnes() {
        // Arrange
        MockEnvironment environment = new MockEnvironment().withProperty(PersistenceType.PROPERTY, "memroy");

        // Act
        Throwable thrown = catchThrowable(() -> postProcessor.postProcessEnvironment(environment, new SpringApplication()));

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("memroy")
                .hasMessageContaining("jpa, memory");
    }

    @Test
    void withoutThePropertyOnlyTheJpaAdapterIsRegistered() {
        // Arrange (no persistence property set)
        ApplicationContextRunner runner = contextRunner;

        // Act (start the context) + Assert (inside the callback, while the context is running)
        runner.run(context -> {
            assertThat(context).hasBean("jpaAdapter");
            assertThat(context).doesNotHaveBean("memoryAdapter");
        });
    }

    @Test
    void memoryRegistersOnlyTheInMemoryAdapter() {
        // Arrange
        ApplicationContextRunner runner = contextRunner.withPropertyValues(PersistenceType.PROPERTY + "=memory");

        // Act (start the context) + Assert (inside the callback, while the context is running)
        runner.run(context -> {
            assertThat(context).hasBean("memoryAdapter");
            assertThat(context).doesNotHaveBean("jpaAdapter");
        });
    }

    @Test
    void jpaRegistersOnlyTheJpaAdapter() {
        // Arrange
        ApplicationContextRunner runner = contextRunner.withPropertyValues(PersistenceType.PROPERTY + "=jpa");

        // Act (start the context) + Assert (inside the callback, while the context is running)
        runner.run(context -> {
            assertThat(context).hasBean("jpaAdapter");
            assertThat(context).doesNotHaveBean("memoryAdapter");
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class AdaptersConfiguration {

        @Bean
        @ConditionalOnJpaPersistence
        String jpaAdapter() {
            return "jpa";
        }

        @Bean
        @ConditionalOnInMemoryPersistence
        String memoryAdapter() {
            return "memory";
        }
    }
}
