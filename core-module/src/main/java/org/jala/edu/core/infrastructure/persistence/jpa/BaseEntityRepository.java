package org.jala.edu.core.infrastructure.persistence.jpa;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base Spring Data repository for {@link BaseJpaEntity} subclasses. Module repositories extend it
 * (Spring Data JPA generates the implementation) and may add derived queries.
 */
@NoRepositoryBean
public interface BaseEntityRepository<E extends BaseJpaEntity, EID>
        extends ListCrudRepository<E, EID>, ListPagingAndSortingRepository<E, EID> {
}
