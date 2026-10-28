package org.jala.edu.core.infrastructure.persistence.jpa;

import org.jala.edu.core.domain.model.BaseModel;

/**
 * Converts between a domain model and its JPA entity.
 *
 * @param <T>   the domain model type
 * @param <ID>  the domain identifier type
 * @param <E>   the JPA entity type
 * @param <EID> the JPA entity identifier type
 */
public interface PersistenceMapper<T extends BaseModel<ID>, ID, E extends BaseJpaEntity, EID> {

    E toEntity(T model);

    T toDomain(E entity);

    EID toEntityId(ID id);
}
