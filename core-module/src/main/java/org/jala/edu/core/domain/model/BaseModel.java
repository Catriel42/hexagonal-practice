package org.jala.edu.core.domain.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Base type for every domain model in the system. Holds the identity and audit timestamps,
 * and defines identity-based equality: two models are equal when their ids are equal.
 *
 * @param <ID> the model's identifier type (e.g. a value object such as {@code UserId})
 */
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseModel<ID> {

    @EqualsAndHashCode.Include
    private final ID id;
    private final Instant createdAt;
    private Instant updatedAt;

    protected BaseModel(ID id, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "Id is required");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Update date is required");
    }

    /**
     * Records that the model changed. Subclasses call this from every state-changing behavior.
     */
    protected void touch() {
        this.updatedAt = now();
    }

    /**
     * Current time at microsecond precision, the precision databases store ({@code DATETIME(6)}),
     * so a saved model reads back with identical timestamps.
     */
    protected static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
