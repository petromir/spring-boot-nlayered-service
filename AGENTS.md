# AGENTS.md

Guidance for AI agents working in this repository. A production-ready example of a Spring Boot
service using **N-Layered architecture**.

## Tech Stack

- **Language:** Java 25 (SDKMAN pins `25.0.3-librca` in `.sdkmanrc`; CI uses JDK 25). The
  Maven compiler `java.version` property is set to `25`.
- **Framework:** Spring Boot 4.0.6 (Web, Security, Validation, jOOQ)
- **Build:** Maven via `mvnd` 1.0.5 (Maven Daemon). Use `mvnd` locally; `mvn` in CI.
- **Data access:** jOOQ (type-safe SQL, code generated from the DB schema)
- **Migrations:** Liquibase (default) — Flyway also supported via profiles
- **Database:** PostgreSQL (managed by Spring Boot Docker Compose integration locally)
- **Auth:** JWT (`jjwt` 0.13.0) + Spring Security, stateless
- **Code generation:** OpenAPI Generator (API interfaces & model payloads from `users-api.yaml`)
- **Lombok:** used for `@Value`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`
- **Testing:** Cucumber (BDD, JUnit 4 runner) + Allure reporting
- **Containerization:** Docker + Docker Compose (see `docker/`)

## Architecture — N-Layered

Layers are virtual separations: presentation, business logic, and data access. The codebase is
organized **by feature module** (e.g. `users`), and **within each module by layer**. Dependency
direction is strictly top-down:

```
Controller → Service → Repository → Entity
                ↘ Mapper ↗ (translates between Payloads and Entities)
```

Cross-cutting concerns (`config`, `exception`) live at the root package and are shared by all modules.

### Layer responsibilities

| Layer                | Package                       | Stereotype                      | Responsibility                                                                                                                            |
|----------------------|-------------------------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **Controller**       | `…/<module>/controller`       | `@RestController`               | Implements generated `…Api` interface. Thin: orchestrates service/mapper/repository calls, returns generated payloads. No business logic. |
| **Controller API**   | `…/<module>/controller/api`   | interface (generated)           | OpenAPI-generated REST interfaces with `@RequestMapping`/`@ResponseStatus`. **Do not edit.**                                              |
| **Controller Model** | `…/<module>/controller/model` | classes (generated)             | OpenAPI-generated request/response payloads. **Do not edit.**                                                                             |
| **Service**          | `…/<module>/service`          | `@Service` / `@Component`       | Business logic, orchestration, security integration (e.g. `UserDetailsService`).                                                          |
| **Repository**       | `…/<module>/repository`       | `@Component`                    | Data access via jOOQ `DSLContext`. No Spring Data repositories. Throws `EntityNotFound*` exceptions.                                      |
| **jOOQ generated**   | `…/<module>/repository/jooq`  | generated                       | Table/keys classes generated from the DB schema. **Do not edit.**                                                                         |
| **Entity**           | `…/<module>/entity`           | `@Value`+`@Builder` / `record`  | Immutable domain objects. May implement Spring Security contracts (e.g. `UserDetails`).                                                   |
| **Mapper**           | `…/<module>/mapper`           | `@Component`                    | Converts between generated payloads and entities. May use collaborators (e.g. `PasswordEncoder`).                                         |
| **Config**           | `…/config`                    | `@Configuration` / `@Component` | Security, filters, exception handling, `@ConfigurationProperties`.                                                                        |
| **Exception**        | `…/exception`                 | `RuntimeException` subclasses   | Domain exceptions, translated to HTTP `ProblemDetail` (RFC 7807) by `BaseHttpExceptionHandler`.                                           |

### Rules to follow

- **Controllers must not contain business logic** — delegate to services, mappers, repositories.
- **Controllers implement the generated `…Api` interface** — do not add `@RequestMapping` to the
  controller; the interface already carries mappings and `@ResponseStatus`. Override the interface methods.
- **Repositories use jOOQ `DSLContext` directly** — no `JpaRepository`, no entity-manager. Use the
  generated `Tables.*` constants for type-safe queries.
- **Entities are immutable** — use Lombok `@Value` + `@Builder` (package-private fields, no `private`
  keyword) or Java `record`. Provide factory helpers like `withoutPassword()` when needed.
- **Never expose entities directly as HTTP responses** — always map to a generated payload via a mapper.
- **Never expose or log secrets/JWT keys.** Secrets come from env vars (`.env`, gitignored).
- **Add new exceptions as `RuntimeException` subclasses** under `…/exception` and add a corresponding
  `@ExceptionHandler` in `BaseHttpExceptionHandler` returning `ProblemDetail` with the appropriate
  HTTP status (e.g. `EntityAlreadyExistsException` → `HttpStatus.CONFLICT`; currently unhandled and
  falls through to the generic handler, producing a 500 instead of 409 — pending fix).

### Adding a new feature module

When adding a new bounded context (e.g. `orders`), follow the existing `users` module structure:

1. Create the package `com.petromirdzhunev/<module>/` with sub-packages:
   `controller/`, `service/`, `repository/`, `entity/`, `mapper/`
2. Define the API in a new or existing OpenAPI YAML spec
3. Run `make generate-code` to produce the `controller/api/*` interface and `controller/model/*` payloads
   (adding a new spec file requires a new `<execution>` block in the `generate-code` Maven profile)
4. Implement the **controller** — implements the generated `…Api` interface, thin orchestration only
5. Implement the **service** — business logic, `@Service`/`@Component`, constructor injection
6. Implement the **repository** — `@Component`, uses jOOQ `DSLContext` directly, throws
   `EntityNotFound*`/`EntityAlreadyExistsException`
7. Define the **entity** — `@Value`+`@Builder` or `record`, immutable
8. Implement the **mapper** — `@Component`, converts between controller model payloads and entities
9. Add a new Liquibase migration in `db/changelog/` and include it in `db.changelog-master.yaml`,
   then regenerate jOOQ code with `make generate-code`
10. Add new exceptions under `com.petromirdzhunev.exception` and handlers in `BaseHttpExceptionHandler`

## Project Structure

```
src/main/java/com/petromirdzhunev/
├── SpringBootNlayeredApplication.java        # entry point (@SpringBootApplication)
├── config/                                   # cross-cutting: SecurityConfig, Jwt*, exception handler
├── exception/                                # EntityNotFoundException, EntityAlreadyExistsException
└── users/                                    # feature module
    ├── controller/
    │   ├── UserController.java               # implements UsersApi (generated)
    │   ├── api/                              # GENERATED — OpenAPI interfaces
    │   └── model/                            # GENERATED — OpenAPI payloads
    ├── service/                              # UserService, AuthenticationService
    ├── repository/
    │   ├── AuthUserRepository.java           # jOOQ-based
    │   └── jooq/                             # GENERATED — jOOQ table/keys classes
    ├── entity/                               # AuthUser, AuthUserRole (immutable)
    └── mapper/                               # UserPayloadMapper
src/main/resources/
├── application.yaml                          # multi-document, profile-aware
└── db/changelog/                             # Liquibase migrations (db.changelog-master.yaml & db definitions)
src/test/java/com/petromirdzhunev/            # CucumberRunnerTest, CucumberSpringConfiguration, TestApplication
src/test/resources/
├── features/                                 # Cucumber .feature files
├── application.yaml                          # test profile config
└── .env.test                                 # test env template (committed)
users-api.yaml                                # OpenAPI 3.1 spec — source of truth for the API (root)
docker/                                       # docker-compose.yaml, application/, db-migrations/ (root)
docker/application/Dockerfile                 # app image build (multi-stage, layered JAR) (root)
Makefile                                      # init-env, build, generate-code, clean-database (root)
.sdkmanrc                                     # Java/mvnd versions (root)
.env.example                                  # env template (committed; real .env is gitignored) (root)
```

## Build, Run & Code Generation

| Task            | Command                |
|-----------------|------------------------|
| Init            | `make init-env`        |
| Build           | `make build`           |
| Run locally     | `mvnd spring-boot:run` |
| Regenerate code | `make generate-code`   |
| Run tests       | `mvn clean test`       |
| Clean DB        | `make clean-database`  |

**No lint/typecheck commands configured.** Code quality is enforced via CI tests only.

**Code generation profiles** (Maven `generate-code` profile):
- **OpenAPI Generator** reads `users-api.yaml` → emits `…/controller/api/*` and `…/controller/model/*`.
- **testcontainers-jooq-codegen** spins up a Postgres container, runs Liquibase migrations, then
  generates `…/repository/jooq/*` from the resulting schema. It requires `src/main/resources/.env`
  (loaded by the `env-file-maven-plugin`) to provide `DB_USERNAME`/`DB_PASSWORD`.

## Testing

- **BDD with Cucumber** — scenarios live in `src/test/resources/features/*.feature`; step glue root
  is `com.petromirdzhunev` (`CucumberRunnerTest`). `CucumberSpringConfiguration` boots
  `TestApplication` with `WebEnvironment.DEFINED_PORT` and truncates the DB before each scenario.
- **Allure** results written to `./target/allure-results` (`allure.properties`).
- **Test DB** — Spring Boot Docker Compose integration starts/stops Postgres for the test run
  (`lifecycle-management: start_and_stop` in the test `application.yaml`).

```bash
mvn clean test                   # runs Cucumber tests (used by CI)
```

## Database & Migrations

- Migrations live in `src/main/resources/db/changelog/`, orchestrated by
  `db.changelog-master.yaml` (Liquibase). Add new migrations as additional `include` entries or SQL files.
- **Local dev:** Spring Boot runs migrations on startup via the `liquibase`/`local` profiles and
  manages the Postgres container (started only, not stopped, for fast dev cycles).
- **Tests:** DB is started and stopped per run for a fresh state.
- **CI/CD PRs:** docker-compose disabled (`ci-cd` profile); migrations run via Spring Boot Liquibase.
- **Deployments (dev/stage/prod):** run migrations **before** app deployment using the dedicated
  Docker images in `docker/db-migrations/` (`Dockerfile.liquibase` / `Dockerfile.flyway`), not on
  app startup. See `README.md` for the rationale and run commands.

## Code Conventions

- **Indentation:** tabs in Java files; 2-space YAML.
- **Dependency injection:** constructor injection via Lombok `@RequiredArgsConstructor` with `final`
  fields. No field injection (`@Autowired`, `@Inject`, etc.).
- **Generated payloads** are mutable (OpenAPI generator) — use their fluent setters
  (e.g. `new LoginResponsePayload().token(...)`).
- **Entities** are immutable (`@Value`+`@Builder` or `record`).
- **Lombok `@Value` fields are package-private** — omit the `private` keyword entirely
  (e.g. `Long id;` not `private Long id;`). `@Value` already generates `private final`.
- **Static imports** for jOOQ `Tables.*` constants and `DSL.*` helpers at the top of repositories.
- **jOOQ records are not generated** (`<records>false</records>` in pom.xml). Only `Table`/`Field`/
  `Key`/`Index` classes are emitted. Repositories use `DSLContext` + `Tables.*` + `mapping()` lambdas.
- **Configuration properties** are records with `@ConfigurationProperties` (see `JwtProperties`),
  enabled via `@EnableConfigurationProperties` on the relevant `@Configuration`.
- **Error responses** always use `org.springframework.http.ProblemDetail` (RFC 7807) with a
  `timestamp` and optional `errors` property — produced by `BaseHttpExceptionHandler`.
  `ProblemDetailResponsePayload` in the OpenAPI spec is mapped to Spring's `ProblemDetail` via
  `schemaMappings` in `pom.xml`; do not generate a separate class for it.
- **Security:** stateless JWT (`SessionCreationPolicy.STATELESS`); `JwtAuthenticationFilter` runs on
  `/api/**` except `/api/users/login`. Add new public endpoints to `SecurityConfig.securityFilterChain`.
- **`BaseHttpExceptionHandler` is injected as a collaborator** into `GlobalAuthenticationEntryPoint`
  and `GlobalAccessDeniedHandler` to write `ProblemDetail` responses directly to the servlet output
  stream. Follow this pattern when adding new security error handlers.
- **Servlet path prefix:** `spring.mvc.servlet.path: /api` — all controller endpoints are under `/api`.
- **Public endpoints:** `/api/users/login`, `/health`, `/metrics`, `/prometheus` are unauthenticated.
  Add new public endpoints to both `SecurityConfig.securityFilterChain` AND the regex in
  `JwtAuthenticationFilter.shouldNotFilter` (currently `^/api(?!/users/login$).*$`).
- **Jackson:** `fail-on-unknown-properties: false` — extra fields in request payloads are ignored.
- **`@Transactional`:** use on service methods when multiple DB operations must succeed or fail together.
  Repositories are not transactional by default.
- **No comments** unless strictly necessary; the codebase is comment-light (one `// FIXME` exists in
  `UserPayloadMapper:39` — pending fix).

## Do-Not-Edit (generated code)

Never hand-edit files under these paths — regenerate via `make generate-code` instead:
- `src/main/java/com/petromirdzhunev/<module>/controller/api/**`
- `src/main/java/com/petromirdzhunev/<module>/controller/model/**`
- `src/main/java/com/petromirdzhunev/<module>/repository/jooq/**`

To change the API: edit `users-api.yaml` → `make generate-code`.
To change jOOQ classes: edit the DB schema via a new Liquibase migration → `make generate-code`.

## Environment

- Real `.env` files are gitignored. Templates: `.env.example` (main), `src/test/resources/.env.test` (tests).
- Copy `.env.example` → `src/main/resources/.env` for local dev (the env-file plugin reads it during
  code generation). Key vars: `SPRING_PROFILES_ACTIVE`, `SERVER_PORT`, `DB_URL`, `DB_USERNAME`,
  `DB_PASSWORD`, `DB_CONNECTION_POOL_*`.
- `application.yaml` is multi-document with profiles: `local`, `liquibase`, `flyway`, `ci-cd`,
  `dev,stage,prod`.
- **Docker networks:** the compose file uses external networks (`database_private`, `database`). These
  must exist before running the app locally, using `make init-env` command.

## CI

GitHub Actions (`.github/workflows/ci.yml`) runs `mvn clean test` on PRs targeting `master`.
Dependabot keeps Maven dependencies current (`chore:` commit prefix).
