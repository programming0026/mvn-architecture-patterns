package com.juniverse.architecture.cqrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CQRS (Command Query Responsibility Segregation) con Event Sourcing sencillo:
 * las escrituras (comandos) guardan eventos y las lecturas (consultas) usan un modelo optimizado para leer.
 */
public final class CqrsDemo {

    private CqrsDemo() {
    }

    /** Evento almacenado. */
    public record MoneyDeposited(String account, double amount) {
    }

    /** Lado de escritura: almacén de eventos. */
    public static class CommandSide {
        private final List<MoneyDeposited> events = new ArrayList<>();
        private final QuerySide query;

        /**
         * Crea el lado de escritura.
         *
         * @param query lado de lectura que se mantiene actualizado
         */
        public CommandSide(QuerySide query) {
            this.query = query;
        }

        /**
         * Comando: depositar. Valida, registra el evento y proyecta.
         *
         * @param account cuenta
         * @param amount importe positivo
         */
        public void deposit(String account, double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("El importe debe ser positivo");
            }
            MoneyDeposited event = new MoneyDeposited(account, amount);
            events.add(event);
            query.apply(event);
        }

        /**
         * Número de eventos guardados (el historial completo).
         *
         * @return cantidad de eventos
         */
        public int eventCount() {
            return events.size();
        }
    }

    /** Lado de lectura: proyección con los saldos ya calculados. */
    public static class QuerySide {
        private final Map<String, Double> balances = new HashMap<>();

        void apply(MoneyDeposited event) {
            balances.merge(event.account(), event.amount(), Double::sum);
        }

        /**
         * Consulta: saldo de una cuenta.
         *
         * @param account cuenta
         * @return saldo actual
         */
        public double balance(String account) {
            return balances.getOrDefault(account, 0.0);
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        QuerySide query = new QuerySide();
        CommandSide commands = new CommandSide(query);
        commands.deposit("A-1", 100);
        commands.deposit("A-1", 50);
        commands.deposit("B-2", 30);
        System.out.printf("Saldo A-1: $%.2f, B-2: $%.2f%n", query.balance("A-1"), query.balance("B-2"));
        System.out.println("Eventos guardados: " + commands.eventCount());
    }
}
