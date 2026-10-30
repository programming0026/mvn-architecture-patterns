package com.juniverse.architecture.pipesandfilters;

import java.util.List;
import java.util.function.Function;

/**
 * Pipes and Filters: el procesamiento se divide en pasos independientes (filtros) conectados en secuencia (tuberías).
 */
public final class PipesAndFiltersDemo {

    private PipesAndFiltersDemo() {
    }

    /** Filtro: transforma un texto en otro. */
    @FunctionalInterface
    public interface Filter extends Function<String, String> {
    }

    /** Tubería: aplica los filtros en orden. */
    public static class Pipeline {
        private final List<Filter> filters;

        /**
         * Crea la tubería.
         *
         * @param filters filtros en el orden de ejecución
         */
        public Pipeline(List<Filter> filters) {
            this.filters = filters;
        }

        /**
         * Procesa un texto con todos los filtros.
         *
         * @param input texto original
         * @return texto procesado
         */
        public String process(String input) {
            String result = input;
            for (Filter filter : filters) {
                result = filter.apply(result);
            }
            return result;
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Pipeline pipeline = new Pipeline(List.of(
                String::trim,
                s -> s.replaceAll("\\s+", " "),
                String::toLowerCase,
                s -> s.replace("<script>", "")));
        String entrada = "   HOLA    Mundo <script>   ";
        System.out.println("Entrada: [" + entrada + "]");
        System.out.println("Salida:  [" + pipeline.process(entrada) + "]");
    }
}
