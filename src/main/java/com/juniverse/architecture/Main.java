package com.juniverse.architecture;

import com.juniverse.architecture.layered.LayeredDemo;
import com.juniverse.architecture.hexagonal.HexagonalDemo;
import com.juniverse.architecture.clean.CleanArchitectureDemo;
import com.juniverse.architecture.mvc.MvcDemo;
import com.juniverse.architecture.eventdriven.EventDrivenDemo;
import com.juniverse.architecture.pipesandfilters.PipesAndFiltersDemo;
import com.juniverse.architecture.cqrs.CqrsDemo;
import com.juniverse.architecture.microkernel.MicrokernelDemo;

import java.util.logging.Logger;
/**
 * Ejecuta un ejemplo de cada patrón de arquitectura de software incluido en el proyecto.
 */
public final class Main {
    private static final Logger LOGGER = AppLogger.get(Main.class);


    private Main() {
    }

    private static void demo(String name, Runnable demo) {
        LOGGER.info("\n=== " + name + " ===");
        demo.run();
    }

    /**
     * Punto de entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        demo("Arquitectura en capas", LayeredDemo::run);
        demo("Hexagonal (puertos y adaptadores)", HexagonalDemo::run);
        demo("Clean Architecture", CleanArchitectureDemo::run);
        demo("MVC", MvcDemo::run);
        demo("Orientada a eventos", EventDrivenDemo::run);
        demo("Pipes and Filters", PipesAndFiltersDemo::run);
        demo("CQRS + Event Sourcing", CqrsDemo::run);
        demo("Microkernel (plugins)", MicrokernelDemo::run);
    }
}
