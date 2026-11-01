package com.juniverse.architecture.microkernel;

import java.util.LinkedHashMap;
import java.util.Map;

import java.util.logging.Logger;
import com.juniverse.architecture.AppLogger;
/**
 * Microkernel (arquitectura de plugins): un núcleo mínimo al que se agregan módulos que extienden su funcionalidad.
 */
public final class MicrokernelDemo {
    private static final Logger LOGGER = AppLogger.get(MicrokernelDemo.class);


    private MicrokernelDemo() {
    }

    /** Contrato que cumplen todos los plugins. */
    public interface Plugin {
        /**
         * Nombre del comando que atiende.
         *
         * @return nombre
         */
        String command();

        /**
         * Ejecuta el comando.
         *
         * @param argument argumento
         * @return resultado
         */
        String execute(String argument);
    }

    /** Núcleo: solo registra y despacha. */
    public static class Kernel {
        private final Map<String, Plugin> plugins = new LinkedHashMap<>();

        /**
         * Instala un plugin.
         *
         * @param plugin plugin
         * @return el núcleo (para encadenar)
         */
        public Kernel install(Plugin plugin) {
            plugins.put(plugin.command(), plugin);
            return this;
        }

        /**
         * Ejecuta un comando con el plugin correspondiente.
         *
         * @param command comando
         * @param argument argumento
         * @return resultado o aviso si no hay plugin
         */
        public String run(String command, String argument) {
            Plugin plugin = plugins.get(command);
            return plugin == null ? "Sin plugin para '" + command + "'" : plugin.execute(argument);
        }
    }

    /** Plugin: mayúsculas. */
    public static class UpperPlugin implements Plugin {
        @Override
        public String command() {
            return "mayusculas";
        }

        @Override
        public String execute(String argument) {
            return argument.toUpperCase();
        }
    }

    /** Plugin: invertir. */
    public static class ReversePlugin implements Plugin {
        @Override
        public String command() {
            return "invertir";
        }

        @Override
        public String execute(String argument) {
            return new StringBuilder(argument).reverse().toString();
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Kernel kernel = new Kernel().install(new UpperPlugin()).install(new ReversePlugin());
        LOGGER.info(kernel.run("mayusculas", "java universe"));
        LOGGER.info(kernel.run("invertir", "java universe"));
        LOGGER.info(kernel.run("traducir", "hola"));
    }
}
