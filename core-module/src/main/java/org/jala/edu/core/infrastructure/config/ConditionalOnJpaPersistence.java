package org.jala.edu.core.infrastructure.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Registers the annotated bean only when {@value PersistenceType#PROPERTY} is {@code jpa} or not set.
 * Put it on the JPA adapters.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(name = PersistenceType.PROPERTY, havingValue = "jpa", matchIfMissing = true)
public @interface ConditionalOnJpaPersistence {
}
