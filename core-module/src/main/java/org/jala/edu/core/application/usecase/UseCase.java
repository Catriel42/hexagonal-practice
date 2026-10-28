package org.jala.edu.core.application.usecase;

/**
 * A single application use case: takes one input and produces one output.
 * Implementations are plain classes (no Spring annotations) registered as beans by each module's configuration.
 *
 * @param <Input> the input type, usually a command or query record (or an id)
 * @param <Output> the output type; use {@link Void} (and return {@code null}) when the use case produces no result
 */
@FunctionalInterface
public interface UseCase<Input, Output> {

    Output execute(Input input);
}
