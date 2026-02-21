# kotlin-boilerplate

A Kotlin boilerplate project using Spring Boot and DDD

## Technology and Resources

- [Kotlin 2.2+](https://kotlinlang.org/) - **pre-requisite**
- [Java 21](https://aws.amazon.com/corretto/) - **pre-requisite**
- [Docker](https://www.docker.com/get-started) - **pre-requisite**
- [Docker Compose](https://docs.docker.com/compose/) - **pre-requisite**
- [Spring Boot 4.x](https://spring.io/projects/spring-boot)
- [PostgreSQL 16](https://www.postgresql.org/)
- [Flyway](https://flywaydb.org/)
- [Testcontainers](https://testcontainers.com/)

*Please pay attention on **pre-requisite** resources that you must install/configure.*

## How to install, run and test

### Environment variables

Variable | Description | Available Values | Default Value | Required
--- | --- | --- | --- | ---
APP_PORT | Application port | any port | `8080` | No
SPRING_PROFILES_ACTIVE | Active Spring profile | `dev / test / prod` | `dev` | Yes

*Note: copy `.env.template` to `.env` and fill in the values before running.*

### Commands

Command | Locally | Description
---- | ------- | -------
build | `make build` | compile the project
test | `make test` | run all tests
run | `make run` | run the application (dev profile)
clean | `make clean` | clean build artifacts

> Docker commands (`make up`, `make docker-build`, etc.) are available after PR-02 and PR-07.

*Please check all available commands in the [Makefile](Makefile) for more information.*

## Architecture

This project follows a pragmatic DDD layered architecture:

```
api → application → domain ← infra
```

Layer | Responsibility
--- | ---
`domain/` | Pure Kotlin — entities, value objects, repository interfaces. Zero framework dependencies.
`application/` | Use cases with a single `execute()` method. Orchestrates domain objects.
`infra/` | JPA entities, Spring Data repositories, `RepositoryImpl` bridging domain and persistence.
`api/` | REST controllers, request/response DTOs, global exception handler.
