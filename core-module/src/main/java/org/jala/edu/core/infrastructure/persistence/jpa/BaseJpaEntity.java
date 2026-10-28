package org.jala.edu.core.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Persistence counterpart of {@link org.jala.edu.core.domain.model.BaseModel}: maps the audit timestamps
 * shared by every table ({@code created_at}, {@code updated_at}). Each entity declares its own id.
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseJpaEntity {

    public static final String CREATED_AT = "createdAt";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
