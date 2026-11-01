package com.juniverse.architecture.eventdriven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import java.util.logging.Logger;
import com.juniverse.architecture.AppLogger;
/**
 * Arquitectura orientada a eventos: los componentes se comunican publicando y consumiendo eventos
 * a través de un bus, sin conocerse entre sí.
 */
public final class EventDrivenDemo {
    private static final Logger LOGGER = AppLogger.get(EventDrivenDemo.class);


    private EventDrivenDemo() {
    }

    /** Evento de dominio. */
    public record OrderPlaced(String orderId, double total) {
    }

    /** Bus de eventos en memoria. */
    public static class EventBus {
        private final Map<Class<?>, List<Consumer<Object>>> handlers = new HashMap<>();

        /**
         * Suscribe un manejador a un tipo de evento.
         *
         * @param type tipo de evento
         * @param handler manejador
         * @param <E> tipo del evento
         */
        @SuppressWarnings("unchecked")
        public <E> void subscribe(Class<E> type, Consumer<E> handler) {
            handlers.computeIfAbsent(type, k -> new ArrayList<>()).add(e -> handler.accept((E) e));
        }

        /**
         * Publica un evento a todos sus suscriptores.
         *
         * @param event evento
         */
        public void publish(Object event) {
            handlers.getOrDefault(event.getClass(), List.of()).forEach(h -> h.accept(event));
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        EventBus bus = new EventBus();
        bus.subscribe(OrderPlaced.class, e -> LOGGER.info("  Facturación: factura para " + e.orderId()));
        bus.subscribe(OrderPlaced.class, e -> LOGGER.info(String.format("  Inventario: reservar artículos de %s ($%.2f)", e.orderId(), e.total())));
        bus.subscribe(OrderPlaced.class, e -> LOGGER.info("  Correo: confirmación enviada de " + e.orderId()));
        LOGGER.info("Se publica OrderPlaced:");
        bus.publish(new OrderPlaced("PED-1001", 1499.90));
    }
}
