package org.jala.edu.core.infrastructure.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Registers the annotated bean only when {@value PersistenceType#PROPERTY} is {@code memory}.
 * Put it on the in-memory adapters.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(name = PersistenceType.PROPERTY, havingValue = "memory")
public @interface ConditionalOnInMemoryPersistence {
}
