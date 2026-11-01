package com.juniverse.architecture.clean;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import com.juniverse.architecture.AppLogger;
/**
 * Clean Architecture: capas concéntricas y la regla de dependencia —el código solo apunta hacia adentro—.
 * Entidades (centro) ← casos de uso ← adaptadores de interfaz ← frameworks y drivers (afuera).
 */
public final class CleanArchitectureDemo {
    private static final Logger LOGGER = AppLogger.get(CleanArchitectureDemo.class);


    private CleanArchitectureDemo() {
    }

    // ---- Círculo 1: entidades (reglas de negocio de la empresa)

    /** Entidad. */
    public record Todo(String title, boolean done) {
        /**
         * Marca la tarea como completada.
         *
         * @return nueva tarea completada
         */
        public Todo complete() {
            return new Todo(title, true);
        }
    }

    // ---- Círculo 2: casos de uso (reglas de la aplicación) y sus límites

    /** Límite de salida que define el caso de uso (lo implementa una capa externa). */
    public interface TodoGateway {
        /**
         * Guarda una tarea.
         *
         * @param todo tarea
         */
        void save(Todo todo);

        /**
         * Lista las tareas.
         *
         * @return tareas
         */
        List<Todo> all();
    }

    /** Caso de uso. */
    public static class AddTodoUseCase {
        private final TodoGateway gateway;

        /**
         * Crea el caso de uso.
         *
         * @param gateway límite de salida
         */
        public AddTodoUseCase(TodoGateway gateway) {
            this.gateway = gateway;
        }

        /**
         * Agrega una tarea pendiente.
         *
         * @param title título obligatorio
         */
        public void execute(String title) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("El título es obligatorio");
            }
            gateway.save(new Todo(title.trim(), false));
        }
    }

    // ---- Círculo 3: adaptadores de interfaz

    /** Presentador: da formato a las entidades para la vista. */
    public static class TodoPresenter {
        /**
         * Convierte una tarea en texto.
         *
         * @param todo tarea
         * @return línea de texto
         */
        public String present(Todo todo) {
            return (todo.done() ? "[x] " : "[ ] ") + todo.title();
        }
    }

    // ---- Círculo 4: frameworks y drivers

    /** Implementación concreta del gateway (en memoria). */
    public static class InMemoryTodoGateway implements TodoGateway {
        private final List<Todo> todos = new ArrayList<>();

        @Override
        public void save(Todo todo) {
            todos.add(todo);
        }

        @Override
        public List<Todo> all() {
            return List.copyOf(todos);
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        InMemoryTodoGateway gateway = new InMemoryTodoGateway();
        AddTodoUseCase add = new AddTodoUseCase(gateway);
        add.execute("Estudiar patrones");
        add.execute("Practicar Java 21");
        TodoPresenter presenter = new TodoPresenter();
        gateway.all().forEach(t -> LOGGER.info(presenter.present(t)));
        LOGGER.info(presenter.present(gateway.all().get(0).complete()));
    }
}
