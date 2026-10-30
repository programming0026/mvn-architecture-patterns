package com.juniverse.architecture;

import com.juniverse.architecture.layered.LayeredDemo;
import com.juniverse.architecture.hexagonal.HexagonalDemo;
import com.juniverse.architecture.clean.CleanArchitectureDemo;
import com.juniverse.architecture.mvc.MvcDemo;
import com.juniverse.architecture.eventdriven.EventDrivenDemo;
import com.juniverse.architecture.pipesandfilters.PipesAndFiltersDemo;
import com.juniverse.architecture.cqrs.CqrsDemo;
import com.juniverse.architecture.microkernel.MicrokernelDemo;

/**
 * Ejecuta un ejemplo de cada patrón de arquitectura de software incluido en el proyecto.
 */
public final class Main {

    private Main() {
    }

    private static void demo(String name, Runnable demo) {
        System.out.println("\n=== " + name + " ===");
        demo.run();
    }

    /**
     * Punto de entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        // Salida en UTF-8 para que se vean bien los acentos en cualquier consola
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
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
