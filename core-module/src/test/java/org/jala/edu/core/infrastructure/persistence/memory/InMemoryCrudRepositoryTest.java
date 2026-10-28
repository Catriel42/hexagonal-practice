package org.jala.edu.core.infrastructure.persistence.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jala.edu.core.domain.model.BaseModel;
import org.junit.jupiter.api.Test;

/**
 * Exercises the generic in-memory adapter with a minimal sample model.
 */
class InMemoryCrudRepositoryTest {

    private static final Instant SAME_TICK = Instant.parse("2026-01-01T10:00:00Z");

    private final NoteRepository repository = new NoteRepository();

    @Test
    void findByIdReturnsTheSavedModel() {
        // Arrange
        Note note = repository.save(new Note(SAME_TICK));

        // Act
        Optional<Note> found = repository.findById(note.getId());

        // Assert
        assertThat(found).contains(note);
    }

    @Test
    void findByIdIsEmptyForAnUnknownId() {
        // Arrange
        repository.save(new Note(SAME_TICK));

        // Act
        Optional<Note> found = repository.findById("unknown");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void existsByIdIsTrueForASavedModel() {
        // Arrange
        Note note = repository.save(new Note(SAME_TICK));

        // Act
        boolean exists = repository.existsById(note.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void deleteByIdRemovesOnlyThatModel() {
        // Arrange
        Note toDelete = repository.save(new Note(SAME_TICK));
        Note toKeep = repository.save(new Note(SAME_TICK));

        // Act
        repository.deleteById(toDelete.getId());

        // Assert
        assertThat(repository.existsById(toDelete.getId())).isFalse();
        assertThat(repository.findAll()).containsExactly(toKeep);
    }

    @Test
    void findAllReturnsModelsOldestFirst() {
        // Arrange
        Note newer = repository.save(new Note(SAME_TICK.plusSeconds(60)));
        Note older = repository.save(new Note(SAME_TICK));

        // Act
        List<Note> notes = repository.findAll();

        // Assert
        assertThat(notes).containsExactly(older, newer);
    }

    @Test
    void findAllKeepsInsertionOrderForModelsCreatedInTheSameTickEvenAfterUpdates() {
        // Arrange
        Note first = repository.save(new Note(SAME_TICK));
        Note second = repository.save(new Note(SAME_TICK));
        Note third = repository.save(new Note(SAME_TICK));
        repository.save(first);

        // Act
        List<Note> notes = repository.findAll();

        // Assert
        assertThat(notes).containsExactly(first, second, third);
    }

    static final class Note extends BaseModel<String> {

        Note(Instant createdAt) {
            super(UUID.randomUUID().toString(), createdAt, createdAt);
        }
    }

    static final class NoteRepository extends InMemoryCrudRepository<Note, String> {
    }
}
