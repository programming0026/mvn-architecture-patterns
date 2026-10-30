# Patrones de arquitectura en Java

Un ejemplo ejecutable y comentado de los patrones de **arquitectura de software** más usados, escritos con Java 21 y sin dependencias externas.

## Patrones incluidos

| Patrón | Archivo | Idea |
|--------|---------|------|
| Arquitectura en capas | `layered/LayeredDemo.java` | Presentación → negocio → datos |
| Hexagonal (puertos y adaptadores) | `hexagonal/HexagonalDemo.java` | El dominio no depende de la tecnología |
| Clean Architecture | `clean/CleanArchitectureDemo.java` | Capas concéntricas y regla de dependencia |
| MVC | `mvc/MvcDemo.java` | Modelo, vista y controlador |
| Orientada a eventos | `eventdriven/EventDrivenDemo.java` | Bus de eventos con publicadores y suscriptores |
| Pipes and Filters | `pipesandfilters/PipesAndFiltersDemo.java` | Filtros independientes conectados en secuencia |
| CQRS + Event Sourcing | `cqrs/CqrsDemo.java` | Escritura con eventos y lectura con proyecciones |
| Microkernel (plugins) | `microkernel/MicrokernelDemo.java` | Núcleo mínimo extendido con módulos |

Cada patrón vive en su propio paquete de `com.juniverse.architecture` con una clase `XxxDemo` que contiene los componentes del patrón y un método `run()` que lo demuestra.

## Requisitos

- JDK 21 y Maven 3.9 o superior.

## Compilar y ejecutar

```bash
mvn compile exec:java

# o como JAR ejecutable (fat jar)
mvn clean package
java -jar target/mvn-architecture-patterns-1.0.0-SNAPSHOT-launcher.jar
```

## Cómo elegir

- **Capas** y **hexagonal/limpia**: para organizar una aplicación de negocio; las dos últimas protegen el dominio de la tecnología.
- **MVC**: para separar la interfaz de usuario de los datos.
- **Eventos** y **CQRS**: cuando los componentes deben desacoplarse o las lecturas y escrituras tienen necesidades distintas.
- **Pipes and Filters**: para procesar datos en pasos reutilizables.
- **Microkernel**: cuando la aplicación debe poder extenderse con módulos.

Mira también `mvn-design-patterns` (los 23 patrones de diseño del GoF).

## Licencia

Código de ejemplo de uso libre con fines educativos.
