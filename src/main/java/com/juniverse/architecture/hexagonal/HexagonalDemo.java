package com.juniverse.architecture.hexagonal;

import java.util.HashMap;
import java.util.Map;

/**
 * Arquitectura hexagonal (puertos y adaptadores): el dominio no depende de la tecnología.
 * Los puertos son interfaces; los adaptadores (consola, base de datos en memoria...) se conectan desde fuera.
 */
public final class HexagonalDemo {

    private HexagonalDemo() {
    }

    /** Dominio. */
    public record Account(String id, double balance) {
    }

    /** Puerto de salida: lo que el dominio necesita de la persistencia. */
    public interface AccountRepositoryPort {
        /**
         * Busca una cuenta.
         *
         * @param id identificador
         * @return la cuenta o {@code null}
         */
        Account find(String id);

        /**
         * Guarda una cuenta.
         *
         * @param account cuenta
         */
        void save(Account account);
    }

    /** Puerto de entrada: lo que la aplicación ofrece al exterior. */
    public interface DepositUseCase {
        /**
         * Deposita dinero.
         *
         * @param id cuenta
         * @param amount importe positivo
         * @return saldo resultante
         */
        double deposit(String id, double amount);
    }

    /** Núcleo de la aplicación: solo conoce puertos. */
    public static class AccountService implements DepositUseCase {
        private final AccountRepositoryPort repository;

        /**
         * Crea el servicio.
         *
         * @param repository puerto de persistencia
         */
        public AccountService(AccountRepositoryPort repository) {
            this.repository = repository;
        }

        @Override
        public double deposit(String id, double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("El importe debe ser positivo");
            }
            Account current = repository.find(id);
            Account updated = new Account(id, (current == null ? 0 : current.balance()) + amount);
            repository.save(updated);
            return updated.balance();
        }
    }

    /** Adaptador de salida: persistencia en memoria (podría ser JPA, Mongo...). */
    public static class InMemoryAccountRepository implements AccountRepositoryPort {
        private final Map<String, Account> data = new HashMap<>();

        @Override
        public Account find(String id) {
            return data.get(id);
        }

        @Override
        public void save(Account account) {
            data.put(account.id(), account);
        }
    }

    /** Adaptador de entrada: interfaz de consola (podría ser REST, mensajería...). */
    public static class ConsoleAdapter {
        private final DepositUseCase useCase;

        /**
         * Crea el adaptador.
         *
         * @param useCase puerto de entrada
         */
        public ConsoleAdapter(DepositUseCase useCase) {
            this.useCase = useCase;
        }

        /**
         * Atiende un comando "depositar".
         *
         * @param id cuenta
         * @param amount importe
         */
        public void deposit(String id, double amount) {
            System.out.printf("Depósito de $%.2f en %s -> saldo $%.2f%n", amount, id, useCase.deposit(id, amount));
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        ConsoleAdapter console = new ConsoleAdapter(new AccountService(new InMemoryAccountRepository()));
        console.deposit("A-1", 500);
        console.deposit("A-1", 250);
    }
}
