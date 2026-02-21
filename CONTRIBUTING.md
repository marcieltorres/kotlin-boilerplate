# Contributing

## Getting Started

### Pre-requisites

- [Java 21](https://adoptium.net/) (Eclipse Temurin recommended)
- [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/)
- [Make](https://www.gnu.org/software/make/)

### Running locally

```bash
# Build the project
make build

# Run the application (dev profile)
make run
```

## Running Tests

```bash
make test
```

Tests are split into two categories:

- **Unit tests** — fast, no external dependencies. Located alongside production code.
- **Integration tests** — use Testcontainers to spin up PostgreSQL. Located in `src/test/` and annotated with `@SpringBootTest`.

## Branching

Use the following naming conventions for branches:

| Prefix | When to use |
| --- | --- |
| `feat/` | New features (e.g., `feat/user-authentication`) |
| `fix/` | Bug fixes (e.g., `fix/login-null-pointer`) |
| `chore/` | Maintenance, deps, config (e.g., `chore/upgrade-spring-boot`) |
| `docs/` | Documentation only (e.g., `docs/update-readme`) |

## Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>: <short description>

[optional body]
```

Common types:

| Type | When to use |
| --- | --- |
| `feat:` | Introduces a new feature |
| `fix:` | Fixes a bug |
| `chore:` | Build process or tooling changes |
| `docs:` | Documentation changes only |
| `refactor:` | Code change that neither fixes a bug nor adds a feature |
| `test:` | Adding or updating tests |

Example:

```
feat: add user registration endpoint

Implements POST /users with email/password validation and BCrypt hashing.
```

## Pull Requests

- Keep PRs small and focused on a single concern.
- All CI checks must pass before merging.
- Request at least one review before merging into `main`.
- Reference the related issue or plan task in the PR description.
