package com.juniverse.architecture.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import java.util.logging.Logger;
import com.juniverse.architecture.AppLogger;
/**
 * MVC (Modelo-Vista-Controlador): separa los datos, su presentación y la lógica que reacciona a la entrada del usuario.
 */
public final class MvcDemo {
    private static final Logger LOGGER = AppLogger.get(MvcDemo.class);


    private MvcDemo() {
    }

    /** Modelo: datos y reglas; avisa a las vistas cuando cambia. */
    public static class CounterModel {
        private int value;
        private final List<Consumer<Integer>> listeners = new ArrayList<>();

        /**
         * Registra una vista interesada en los cambios.
         *
         * @param listener función que recibe el nuevo valor
         */
        public void onChange(Consumer<Integer> listener) {
            listeners.add(listener);
        }

        /**
         * Suma una cantidad.
         *
         * @param delta cantidad
         */
        public void add(int delta) {
            value += delta;
            listeners.forEach(l -> l.accept(value));
        }
    }

    /** Vista: solo muestra. */
    public static class CounterView {
        /**
         * Muestra el valor.
         *
         * @param value valor actual
         */
        public void render(int value) {
            LOGGER.info("Contador = " + value);
        }
    }

    /** Controlador: traduce la entrada del usuario en acciones sobre el modelo. */
    public static class CounterController {
        private final CounterModel model;

        /**
         * Crea el controlador y conecta modelo y vista.
         *
         * @param model modelo
         * @param view vista
         */
        public CounterController(CounterModel model, CounterView view) {
            this.model = model;
            model.onChange(view::render);
        }

        /**
         * Atiende un comando del usuario.
         *
         * @param command "+" para sumar, "-" para restar
         */
        public void handle(String command) {
            switch (command) {
                case "+" -> model.add(1);
                case "-" -> model.add(-1);
                default -> LOGGER.info("Comando desconocido: " + command);
            }
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        CounterController controller = new CounterController(new CounterModel(), new CounterView());
        controller.handle("+");
        controller.handle("+");
        controller.handle("-");
        controller.handle("x");
    }
}
