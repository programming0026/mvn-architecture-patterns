package com.juniverse.architecture.layered;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import com.juniverse.architecture.AppLogger;
/**
 * Arquitectura en capas: presentación → negocio → datos. Cada capa solo conoce a la de abajo.
 */
public final class LayeredDemo {
    private static final Logger LOGGER = AppLogger.get(LayeredDemo.class);


    private LayeredDemo() {
    }

    /** Capa de datos. */
    public static class ProductRepository {
        private final List<String> products = new ArrayList<>(List.of("Teclado", "Ratón"));

        /**
         * Todos los productos.
         *
         * @return lista de nombres
         */
        public List<String> findAll() {
            return List.copyOf(products);
        }

        /**
         * Guarda un producto.
         *
         * @param name nombre
         */
        public void save(String name) {
            products.add(name);
        }
    }

    /** Capa de negocio: reglas de la aplicación. */
    public static class ProductService {
        private final ProductRepository repository;

        /**
         * Crea el servicio.
         *
         * @param repository capa de datos
         */
        public ProductService(ProductRepository repository) {
            this.repository = repository;
        }

        /**
         * Regla de negocio: no se aceptan nombres vacíos ni repetidos.
         *
         * @param name nombre del producto
         */
        public void register(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }
            if (repository.findAll().contains(name)) {
                throw new IllegalStateException("Ya existe: " + name);
            }
            repository.save(name);
        }

        /**
         * Catálogo.
         *
         * @return productos registrados
         */
        public List<String> catalog() {
            return repository.findAll();
        }
    }

    /** Capa de presentación. */
    public static class ProductConsole {
        private final ProductService service;

        /**
         * Crea la presentación.
         *
         * @param service capa de negocio
         */
        public ProductConsole(ProductService service) {
            this.service = service;
        }

        /**
         * Registra un producto y muestra el resultado o el error.
         *
         * @param name nombre
         */
        public void add(String name) {
            try {
                service.register(name);
                LOGGER.info("Registrado: " + name);
            } catch (RuntimeException e) {
                LOGGER.info("Error: " + e.getMessage());
            }
        }

        /** Imprime el catálogo. */
        public void list() {
            LOGGER.info("Catálogo: " + service.catalog());
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        ProductConsole ui = new ProductConsole(new ProductService(new ProductRepository()));
        ui.add("Monitor");
        ui.add("Teclado");
        ui.add(" ");
        ui.list();
    }
}
