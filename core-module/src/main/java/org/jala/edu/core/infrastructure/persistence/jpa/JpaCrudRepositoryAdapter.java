package org.jala.edu.core.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jala.edu.core.application.port.out.CrudRepositoryPort;
import org.jala.edu.core.domain.model.BaseModel;
import org.springframework.data.domain.Sort;

/**
 * Base JPA adapter for {@link CrudRepositoryPort}: delegates to a Spring Data repository and converts
 * with a {@link PersistenceMapper}. Module adapters extend it and add their own queries.
 */
public abstract class JpaCrudRepositoryAdapter<T extends BaseModel<ID>, ID, E extends BaseJpaEntity, EID>
        implements CrudRepositoryPort<T, ID> {

    private static final Sort OLDEST_FIRST = Sort.by(Sort.Direction.ASC, BaseJpaEntity.CREATED_AT);

    private final BaseEntityRepository<E, EID> repository;
    private final PersistenceMapper<T, ID, E, EID> mapper;

    protected JpaCrudRepositoryAdapter(BaseEntityRepository<E, EID> repository,
                                       PersistenceMapper<T, ID, E, EID> mapper) {
        this.repository = Objects.requireNonNull(repository, "Repository is required");
        this.mapper = Objects.requireNonNull(mapper, "Mapper is required");
    }

    @Override
    public T save(T model) {
        return mapper.toDomain(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(mapper.toEntityId(id)).map(mapper::toDomain);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll(OLDEST_FIRST).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(mapper.toEntityId(id));
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(mapper.toEntityId(id));
    }
}
