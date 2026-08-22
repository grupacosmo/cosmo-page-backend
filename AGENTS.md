# AGENTS.md — cosmo-page-backend

Onboarding for AI agents (and humans) working on this repository.

## What this is

`cosmo-page-backend` is a **Spring Boot 3.5.16 / Java 21** REST backend for the cosmo-page
infrastructure. It manages posts synced from a Facebook page (webhook + manual sync), users,
images and mail history, and talks to the Facebook Graph API.

There is a **separate Angular frontend** (`cosmo-page-frontend`) that is **out of scope** — do not
touch, modify or create anything in it. Treat its API expectations as a hard constraint (see
[Frontend compatibility](#frontend-compatibility)).

## Quick start (commands that must work)

```shell
# full build: unit + integration + contract tests + coverage gate (needs Docker)
./mvnw clean verify

# unit tests only
./mvnw test

# a single integration test class
./mvnw verify -Dit.test='PostIT'

# run locally with hot reload (requires: docker compose up -d postgres)
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run    # Linux/macOS
$env:SPRING_PROFILES_ACTIVE="local"; .\mvnw.cmd spring-boot:run   # Windows

# whole local stack (Postgres + MailHog + backend), no env needed
docker compose up -d --build
```

- **Always run `./mvnw clean verify` after changes** — it is the lint/typecheck gate. If you
  cannot run Docker, at minimum run `./mvnw test` and `./mvnw -DskipTests package`.
- The Maven wrapper downloads its own Maven; a global Maven install is not required.
- Local toolchain note: building on **JDK 25+** requires the pinned compiler/lombok versions already
  in `pom.xml` (do not downgrade them).

## Architecture at a glance

- **Controllers** live under `src/main/java/com/webdev/cosmo/cosmobackend/service/**` and are plain
  `@RestController`s (they do **not** implement the OpenAPI-generated interfaces).
- **Models** used in controllers are generated from the **canonical OpenAPI spec**
  `src/main/resources/schemas.yml` (openapi-generator-maven-plugin) into
  `target/generated-sources/openapi` (package `org.openapitools.model`). The spec is the source of
  truth — edit the spec, never the generated code.
- Entities (`Post`, `User`, `Token`, `Image`, `FacebookImage`, `Mail`) are in `service/api` /
  `service/internal/**/model`.
- Wiring style: constructor injection via Lombok `@RequiredArgsConstructor`; entities use
  `@Data @Accessors(chain = true)`; DTO mapping via MapStruct mappers.
- Auth has two layers: an **API-key filter** (`security/filters/ApiKeyFilter.java`) and a
  **Facebook user-authentication filter** (`security/filters/UserAuthenticationFilter.java`) for
  the `/user` endpoints (requires `user_id` + `access_token` headers).
- There is also a Facebook **emulator** (`emulator/`) active in `local`/`test` profiles — it makes
  the whole flow work without real Facebook credentials.

## Profiles

| Profile | Used for | Datasource | Facebook | Flyway / DDL | API key |
| --- | --- | --- | --- | --- | --- |
| `local` | dev | `localhost:5432` (`postgres`/`postgres`) | emulated | `ddl-auto: update`, Flyway off | not required |
| `test` | integration tests only (`@ActiveProfiles("test")`) | Testcontainers Postgres | emulated | Flyway on + `ddl-auto: validate` | required (`test-api-key`) |
| `prod` | production | `POSTGRES_URL` env | real | Flyway on + `ddl-auto: validate` | required (fail-closed) |

Config lives in `application.yml` (base) + `application-{profile}.yml`. The `test` profile file is
in `src/integration-tests/resources/`.

## Schema changes (IMPORTANT)

`prod` and `test` run **Flyway** with `spring.jpa.hibernate.ddl-auto: validate`. When you change an
entity, you **must** add a new migration:

```
src/main/resources/db/migration/V<next>__description.sql
```

Do **not** rely on `ddl-auto: update` in prod (it is only used by `local`). If a migration is
missing, the app will fail to start with a schema validation error — that is the intended safety
net. The existing `V1__baseline.sql` was generated from the JPA metadata; keep migrations in sync
with the entities.

## Frontend compatibility

The Angular frontend consumes exactly two endpoints. Never change their shapes:

- `GET /api/posts?page=&size=` → the full Spring `Page` JSON envelope
  (`content, pageable{...}, totalPages, totalElements, last, numberOfElements, size, number, sort,
  first, empty`). In particular `sort` is an **array** and `pageable.sort` is an **array**.
- `GET /api/posts/{id}` → `{ id, title, description, images: [{height,width,src}] }`.

Also keep the **`apiKey` header** auth mechanism intact (frontend sends `apiKey: ...` on every
request). Any change to these endpoints will break the frontend — the contract tests in
`ContractsIT` guard the posts shapes.

## Contract tests (do not break them)

`src/integration-tests/java/com/webdev/cosmo/cosmobackend/contract/OpenApiContract.java` validates
live HTTP responses against `schemas.yml`. When you change an endpoint or `schemas.yml`, make sure
`./mvnw verify -Dit.test='ContractsIT'` still passes. `skipValidateSpec` is **false** — the spec
must stay valid.

## Key behaviors worth knowing before editing

- `PUT /api/posts/sync` returns `202` and is **transactional** (`PostsSyncExecutor`).
- `POST /api/user`, `GET /api/user/{email}`, `PUT /api/user`, `DELETE /api/user/{email}` require
  `user_id` + `access_token` headers (Facebook auth). In `local`/`test` any values pass (emulator).
- `POST /api/facebook/token` stores a page token in the DB; at startup the app falls back to
  `FB_PAGE_TOKEN` + `FB_PAGE_ID` env if the DB is empty.
- The webhook handshake (`GET /api/facebook/notif`) echoes `hub.challenge` only when
  `hub.verify_token` matches `facebook.notif-token` (`FB_TOKEN`; defaults to `local-webhook-token`
  in `local`).
- `MailController` only **saves** mail records (`POST /api/mail`); sending requires an SMTP server.
- The `/api/user-privacy/*` and `/actuator/health` paths are exempt from the API-key filter.

## Environment & secrets

- Copy `.env.example` → `.env` for local env overrides (loaded via `spring-dotenv`). `.env` is
  gitignored. Never commit `.env` or real tokens.
- Prod env vars are read by the CI deploy from `/cosmo/.env-prod` on the server.
- Useful vars: `POSTGRES_URL`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD`, `API_KEYS`,
  `API_KEYS_REQUIRED` (`true` in prod → fail closed), `CORS_ALLOWED_ORIGINS`, `FB_TOKEN`,
  `FB_PAGE_TOKEN`, `FB_PAGE_ID`, `CLIENT_ID`, `CLIENT_SECRET`, `PROD_URL`, `MAIL_*`.
- Security note: do not loosen `ApiKeyFilter` to fail-open, and do not re-add "smoke"/demo
  endpoints that send to hardcoded recipients.

## Observability

- Every response carries `X-Request-Id`; the same id is in the logs under the `requestId` MDC key.
  Use it to trace a single failing request end-to-end.
- `GET /actuator/info` reports the deployed git commit (via `git.properties`). Prod exposes only
  `health` and `info`.

## Manual testing

A ready **Postman collection** with every endpoint and sample bodies is at
`postman/cosmo-page-backend.postman_collection.json`. Start the local stack
(`docker compose up -d --build`), import the collection, click through. Swagger UI is available
locally at `http://localhost:8080/swagger-ui.html` (disabled in prod).

## CI/CD (Github Actions)

- `.github/workflows/ci.yml`: `test` (all tests + coverage) → `docker-build-check` (PRs) →
  `docker-build` (master, immutable `sha-<commit>` + `latest` tags, Trivy scan) → `deploy` (master,
  smoke tests against a candidate container, rollback-by-design). Supports manual
  redeploy/rollback via `workflow_dispatch` with a `tag` input.
- `.github/workflows/codeql.yml` and `dependency-review.yml` run security checks.
- `dependabot.yml` keeps Maven + Actions dependencies updated.
- Do not downgrade pinned actions below their current majors without reason.

## Suggested first tasks

- Understand the flow: webhook → `FacebookWebhookController` → `WebhookNotificationConsumer` →
  `PostsSyncExecutor` → `PostRepository`.
- To add an endpoint: (1) controller, (2) model/schema in `schemas.yml`, (3) service,
  (4) a `ContractsIT`/`PostIT`-style test, (5) `./mvnw clean verify` green.