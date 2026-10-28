package org.jala.edu.core.infrastructure.persistence.memory;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.jala.edu.core.application.port.out.CrudRepositoryPort;
import org.jala.edu.core.domain.model.BaseModel;

/**
 * Base in-memory adapter for {@link CrudRepositoryPort}. Module adapters extend it and implement their own
 * queries on top of {@link #stream()}. Data is lost when the application stops.
 */
public abstract class InMemoryCrudRepository<T extends BaseModel<ID>, ID> implements CrudRepositoryPort<T, ID> {

    /**
     * Oldest first; models created within the same clock tick keep their insertion order.
     */
    private static final Comparator<Stored<?>> OLDEST_FIRST = Comparator
            .<Stored<?>, Instant>comparing(stored -> stored.model().getCreatedAt())
            .thenComparingLong(Stored::sequence);

    private final Map<ID, Stored<T>> store = new ConcurrentHashMap<>();
    private final AtomicLong nextSequence = new AtomicLong();

    @Override
    public T save(T model) {
        store.compute(model.getId(), (id, previous) ->
                new Stored<>(model, previous == null ? nextSequence.getAndIncrement() : previous.sequence()));
        return model;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id)).map(Stored::model);
    }

    @Override
    public List<T> findAll() {
        return store.values().stream().sorted(OLDEST_FIRST).map(Stored::model).toList();
    }

    @Override
    public boolean existsById(ID id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(ID id) {
        store.remove(id);
    }

    protected Stream<T> stream() {
        return store.values().stream().map(Stored::model);
    }

    private record Stored<M extends BaseModel<?>>(M model, long sequence) {
    }
}
