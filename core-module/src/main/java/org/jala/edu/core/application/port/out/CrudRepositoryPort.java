package org.jala.edu.core.application.port.out;

import java.util.List;
import java.util.Optional;
import org.jala.edu.core.domain.model.BaseModel;

/**
 * Generic outbound port for storing models. Module ports extend it and add their own queries.
 *
 * @param <T>  the model type
 * @param <ID> the model's identifier type
 */
public interface CrudRepositoryPort<T extends BaseModel<ID>, ID> {

    /**
     * Inserts a new model or replaces the stored one with the same id.
     */
    T save(T model);

    Optional<T> findById(ID id);

    /**
     * Returns every model ordered by creation date, oldest first.
     */
    List<T> findAll();

    boolean existsById(ID id);

    void deleteById(ID id);
}
