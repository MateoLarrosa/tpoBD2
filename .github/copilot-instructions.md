# Copilot Instructions for AI Coding Agents

## Project Overview
- This is a Java application organized by domain-driven packages under `src/main/java/`.
- Major components:
  - `connections/`: Database connection pools (Cassandra, MongoDB, Redis).
  - `controlador/`: Controllers for the application logic.
  - `modelo/`: Core business models (Usuario, Factura, Mensaje, etc.).
  - `repositories/`: Data access and repository patterns for MongoDB, Cassandra and Redis.
  - `services/`: Business logic
  - `menus/`: Console menu UI and navigation.
  - `main/`: Application entry points (`Main.java`, `MainMensajeria.java`).

## Build & Run
- Build with Maven: `mvn clean package` (requires `pom.xml`).
- Run main app: `./run.sh` or `Main.java`.

## Patterns & Conventions
- Controllers (`controlador/`) orchestrate between services and models. **Controllers must use the Singleton pattern.**
- Services (`services/`) encapsulate business logic, called by controllers. **Services must use the Singleton pattern.**
- Repositories (`repositories/`) abstract data access, use connection pools. **Repositories must use the Singleton pattern.**
- Models (`modelo/`) are POJOs with minimal logic.
- Menu system (`menus/`) is modular, each menu in its own class.
- Exception handling: custom exceptions in `exceptions/`.
- Logging: configuration in `src/main/resources/` (`log4j.properties`, etc.).

## Integration & Data Flow
- MongoDB and Redis are used for persistence and messaging (see `MongoPool`, `RedisPool`).
- Controllers call services, which use repositories for DB access.
- Messaging logic is separated in `Mensajeria*` classes and scripts.
- Cassandra is used for certain data storage needs (see `CassandraPool`).

## Project-Specific Notes
- Scripts (`run.sh`) are the preferred way to launch apps.
- External dependencies managed via Maven (`pom.xml`).
- Docker Compose (`docker-compose.yml`) may be used for local DBs.

## Examples
- To add a new menu: create a class in `menus/`, register in `MenuPrincipal`.
- To add a new DB entity: add model in `modelo/`, repository in `repositories/`, update service/controller as needed.

---
For questions or unclear patterns, ask for clarification or review the relevant package for examples.
