package org.jala.edu.core.infrastructure.ui;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.concurrent.Task;

/**
 * Runs blocking work (use cases, I/O) off the JavaFX application thread.
 * Callbacks are invoked back on the JavaFX application thread, so they may update the UI.
 */
public final class BackgroundTasks {

    private BackgroundTasks() {
    }

    public static <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return work.call();
            }
        };
        task.setOnSucceeded(event -> onSuccess.accept(task.getValue()));
        task.setOnFailed(event -> onFailure.accept(task.getException()));
        Thread.ofVirtual().start(task);
    }
}
