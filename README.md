# cosmo-page-backend

## General Information

Contains an implementation for an API service used by cosmo-page infrastructure.

Before deploying the Spring Boot application, ensure that you have the following prerequisites installed and configured on your deployment environment:

![](./docs/high-level-design.png)

![](./docs/detailed-design.png)

## Prerequisites

- **JDK 21**
- **Docker + Docker Compose** — required for `docker compose` flows and for the integration tests
  (Testcontainers spins up a real Postgres). The application itself can run without Docker.
- **PostgreSQL** (or use the one started by Compose)
- **No global Maven needed** — the project ships the Maven wrapper (`./mvnw` / `mvnw.cmd`).

---

## Running the application

Every supported way to run, test and build the app. Pick the scenario that matches what you want
to do.

| Scenario | Section |
| --- | --- |
| Local development, everything in Docker (easiest) | [1. Full stack via Docker Compose](#1-local-development--full-stack-via-docker-compose) |
| Local development, backend on host (hot reload) | [2. Postgres in Docker + backend on host](#2-local-development--postgres-in-docker--backend-on-host) |
| Run the tests | [3. Tests](#3-tests) |
| Build the jar only | [4. Build](#4-build) |
| Run the jar with the `prod` profile | [5. Run the jar (bare metal)](#5-run-the-jar-bare-metal) |
| Run the Docker image directly | [6. Run the Docker image](#6-run-the-docker-image) |
| Which profile to pick | [7. Profiles](#7-profiles) |
| All configuration knobs | [8. Environment variables](#8-environment-variables) |
| Facebook emulator vs real API | [9. Facebook emulator vs real API](#9-facebook-emulator-vs-real-api) |
| Problems? | [10. Troubleshooting](#10-troubleshooting) |

### 1. Local development — full stack via Docker Compose

The fastest way to start. One command runs **Postgres + the backend**.

```shell
docker compose up -d --build
```

| What | Where |
| --- | --- |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Postgres | `localhost:5432` (`postgres` / `postgres`, DB `cosmo_backend`) |

- The backend starts with the **`local`** profile: Facebook API is **emulated**, sample posts are
  seeded automatically, **no environment variables are required**, and **no API key is required**
  (`api-keys-required=false`).
- Postgres data lives in the named volume `postgres-data`, so it survives `docker compose down`.

> **Click-to-test:** a ready Postman collection with every endpoint, sample bodies and variables is
> shipped in [`postman/cosmo-page-backend.postman_collection.json`](./postman/cosmo-page-backend.postman_collection.json).
> Import it, start the app, and click — no need to type requests by hand.

Useful commands:

```shell
docker compose ps                 # status of all services
docker compose logs -f backend    # follow backend logs
docker compose down               # stop (database data is kept)
docker compose down -v            # stop AND wipe the database volume
docker compose up -d --build      # rebuild and start after code changes
```

### 2. Local development — Postgres in Docker + backend on host

Use this when you want the backend running directly on your machine (faster restarts, hot reload
via `spring-boot-devtools`, easier debugging in your IDE).

1. Start only the database:

```shell
docker compose up -d postgres
```

2. Run the backend from the host with the `local` profile:

```shell
# Linux / macOS
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="local"
.\mvnw.cmd spring-boot:run
```

The `local` profile connects to `jdbc:postgresql://localhost:5432/cosmo_backend`
(`postgres` / `postgres`), enables the Facebook emulator and hot reload.

> Tip: in IntelliJ IDEA you can also create a Spring Boot run configuration and set the **active
> profile** to `local`.

### 3. Tests

All tests need Docker (Testcontainers starts a `postgres:16` container).

```shell
# everything: unit + integration + contract tests + coverage gate
./mvnw clean verify

# unit tests only
./mvnw test

# integration tests only
./mvnw verify -Dit.test='*IT'

# one integration test class
./mvnw verify -Dit.test='PostIT'
```

What runs during `verify`:

- **Unit tests** (`src/test`) — mocked service/mapper logic.
- **Integration tests** (`src/integration-tests`) — real HTTP calls against a real Postgres
  (Testcontainers): posts, users, images, webhook handshake, health.
- **Contract tests** (`ContractsIT`) — validates that HTTP responses match the canonical OpenAPI
  spec (`src/main/resources/schemas.yml`).
- **JaCoCo** — coverage report and a minimum coverage gate.

Reports: test reports in `target/surefire-reports` and `target/failsafe-reports`; coverage report
at `target/site/jacoco/index.html`.

### 4. Build

```shell
# full build (jar + tests)
./mvnw clean install

# build the jar without running tests
./mvnw clean package -DskipTests
```

The executable jar is produced at `target/cosmo-backend-0.0.1-SNAPSHOT.jar`.

### 5. Run the jar (bare metal)

Used for production-like runs outside Docker, or anywhere you run the jar directly.

1. Make sure a Postgres instance is reachable and set the required variables
   ([8. Environment variables](#8-environment-variables)): `POSTGRES_URL`, `POSTGRES_USERNAME`,
   `POSTGRES_PASSWORD`, `API_KEYS`, `PROD_URL`, `FB_TOKEN` (+ `FB_PAGE_TOKEN` / `FB_PAGE_ID` when
   you don't store the token via the API).

2. Run with the `prod` profile:

```shell
# Linux / macOS
SPRING_PROFILES_ACTIVE=prod \
POSTGRES_URL=jdbc:postgresql://... \
POSTGRES_USERNAME=... POSTGRES_PASSWORD=... \
API_KEYS=... PROD_URL=https://cosmopk.pl FB_TOKEN=... \
java -jar target/cosmo-backend-0.0.1-SNAPSHOT.jar

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:POSTGRES_URL="jdbc:postgresql://..."
# ... set the remaining variables ...
java -jar target\cosmo-backend-0.0.1-SNAPSHOT.jar
```

Alternatively use the **`.env` file** — the `spring-dotenv` dependency reads it automatically from
the working directory (see `.env.example`):

```shell
cp .env.example .env    # fill in the values
java -Dspring.profiles.active=prod -jar target/cosmo-backend-0.0.1-SNAPSHOT.jar
```

> In `prod` the database schema is managed by **Flyway** (`ddl-auto: validate`). A fresh database
> is created automatically from `src/main/resources/db/migration`. Swagger is disabled and actuator
> exposes only `health` and `info`.

### 6. Run the Docker image

The image is built from the `Dockerfile` (Java 21, runs as non-root `cosmopk`).

Build the image locally:

```shell
docker build -t cosmopk/cosmo-page-backend .
```

> The build accepts a `GIT_COMMIT` build-arg (the CI pipeline passes `github.sha`). When set it is
> baked into `git.properties` so `GET /actuator/info` reports the deployed commit even though the
> build context contains no `.git` directory. Local builds without the arg report `unknown`.

Run with the `prod` profile (env from a file):

```shell
docker run -d --name cosmo-backend \
  -p 8080:8080 \
  --env-file .env-prod \
  --restart unless-stopped \
  cosmopk/cosmo-page-backend
```

Run locally against Postgres running on the host:

```shell
docker run -d --name cosmo-backend -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/cosmo_backend \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  --restart unless-stopped \
  cosmopk/cosmo-page-backend
```

> `host.docker.internal` points from the container to your machine. On Linux you may need
> `--add-host=host.docker.internal:host-gateway`.

Verify it is up:

```shell
curl http://localhost:8080/actuator/health   # -> {"status":"UP", ...}
```

### 7. Profiles

| Profile | When to use | Datasource | Facebook | Flyway | API key | Swagger |
| --- | --- | --- | --- | --- | --- | --- |
| `local` | local development | `localhost:5432` (`postgres`/`postgres`) | emulated | off (`ddl-auto: update`) | not required | on |
| `test` | integration tests only | Testcontainers Postgres | emulated | on + `ddl-auto: validate` | required | n/a |
| `prod` | production | `POSTGRES_URL` env | real | on + `ddl-auto: validate` | **required** | off |
| *(none)* | manual run with env vars | `POSTGRES_URL` env | real (emulator off by default) | off | optional | on |

The default/base configuration lives in `application.yml`; profile-specific files are
`application-local.yml` and `application-prod.yml`. The `test` profile is only used by the
integration tests.

### 8. Environment variables

| Variable | Required | Description | Default |
| --- | --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | dev/local | active profile (`local` or `prod`) | *none* |
| `POSTGRES_URL` | prod | JDBC URL (`jdbc:postgresql://host:5432/db`) | — |
| `POSTGRES_USERNAME` | prod | DB user | — |
| `POSTGRES_PASSWORD` | prod | DB password | — |
| `API_KEYS` | prod | comma-separated accepted API keys (`apiKey` header) | empty |
| `API_KEYS_REQUIRED` | — | reject requests without a valid key; `true` + empty `API_KEYS` fails startup (fail closed) | `false` (`true` in prod) |
| `CORS_ALLOWED_ORIGINS` | — | comma-separated allowed origins | `http://localhost:4200,https://cosmopk.pl` |
| `FB_TOKEN` | prod | Facebook webhook verify token | empty |
| `FB_PAGE_TOKEN` | optional | page access token (startup fallback) | empty |
| `FB_PAGE_ID` | optional | Facebook page id (startup fallback) | empty |
| `CLIENT_ID` / `CLIENT_SECRET` | optional | Facebook app credentials | empty |
| `PROD_URL` | prod | public URL of the API | `http://localhost:8080` |
| `POSTGRES_HOST_PORT` | local | host port for the compose Postgres | `5432` |
| `JAVA_OPTS` | optional | JVM flags for the container (`java $JAVA_OPTS -jar ...`) | empty |

### 9. Facebook emulator vs real API

- **`local` / `test`** — `facebook.emulator.enabled=true`. The Facebook Graph API is emulated:
  sample posts are served and the database is seeded automatically. `POST /api/posts/sync`,
  `GET /api/posts` and `GET /api/facebook/posts` all work without any Facebook credentials or
  network access to Facebook. The emulator is **opt-in**: the base config disables it, so any
  profile that does not explicitly enable it uses the real client.
- **`prod`** — `facebook.emulator.enabled=false`. The real Graph API is used; `FB_TOKEN` (webhook
  verify) is required and `FB_PAGE_TOKEN` + `FB_PAGE_ID` are used as the startup fallback when no
  token is stored in the database yet.

> **Schema migrations (prod):** the `prod` profile uses **Flyway** migrations
> (`src/main/resources/db/migration`) and `spring.jpa.hibernate.ddl-auto: validate`. Never rely on
> Hibernate auto-DDL in production — add a new `V<next>__*.sql` migration for schema changes.
> Existing databases are automatically baselined (`baseline-on-migrate`), so the first deploy
> requires no manual step.
>
> **API key enforcement:** in `prod` requests without a valid `apiKey` header are rejected
> (`API_KEYS_REQUIRED=true`). If the key list is empty while enforcement is on, the application
> refuses to start — the API fails closed instead of silently accepting every request.

### 10. Troubleshooting

| Problem | Fix |
| --- | --- |
| `Failed to configure a DataSource` at startup | No profile / datasource configured. Set `SPRING_PROFILES_ACTIVE=local` (compose/host) or provide `POSTGRES_URL`+credentials. |
| Backend container restarts / unhealthy | Postgres not ready yet — check `docker compose ps` (Postgres must be `healthy`). |
| `401 Unauthorized` on every request (prod) | `API_KEYS` does not match the `apiKey` header the client sends. |
| App refuses to start with *"API keys are required but no API_KEYS are configured"* | `API_KEYS_REQUIRED=true` with an empty `API_KEYS` — the fail-closed guard rejects this; provide the key list. |
| `401` on `/api/user...` even with a valid `apiKey` | The `user_id` and `access_token` headers are missing or rejected (Facebook user authentication). |
| Swagger UI 404 | Disabled by design in the `prod` profile. Use `local`. |
| Tests fail with "Docker environment" error | Docker daemon not running — start Docker Desktop / Docker engine. |
| Database data gone | `docker compose down -v` wipes the `postgres-data` volume (intentional reset). |
| `Schema validation failed` / Flyway migration error (prod) | The DB schema does not match the entities. Add a new `V<next>__*.sql` migration instead of relying on `ddl-auto`. |
| I want to know which commit is deployed | `curl http://localhost:8080/actuator/info` reports the git commit (prod exposes `health` + `info`). |

---

## Continuous Integration / Delivery

The repository ships several GitHub Actions workflows:

- **`.github/workflows/ci.yml`** — the main pipeline:
  1. **`test`** (every push and PR): full build — unit tests, integration tests against a real
     Postgres (Testcontainers), **contract tests** that validate HTTP responses against the
     canonical OpenAPI spec (`src/main/resources/schemas.yml`), and a JaCoCo coverage gate.
     Reports are uploaded as artifacts.
  2. **`docker-build-check`** (PR only): builds the Docker image without pushing, so Dockerfile
     issues are caught before merge.
  3. **`docker-build`** (push to `master`, after tests pass): builds with Docker layer caching,
     pushes **immutable** tags (`sha-<commit>` + `latest`) and scans the image with **Trivy**
     (results land in the GitHub Security tab).
  4. **`deploy`** (push to `master`): deploys the new tag on the server and runs **smoke tests**
     against the candidate (actuator health, `GET /api/posts` with the API key, Facebook webhook
     handshake), then promotes it and verifies the final container health. If the promoted
     container does not become healthy, the previous image is started again automatically
     (rollback by design). Smoke tests use the first key from `API_KEYS`, so the check works even
     when several keys are configured.

  The pipeline can also be **triggered manually** (`Actions → CI/CD → Run workflow`): pass an
  existing image tag (e.g. `sha-<old-commit>`) to redeploy or **roll back** a specific version
  without touching the code or moving the `latest` tag.
- **`.github/workflows/codeql.yml`** — CodeQL static security analysis on every push/PR and
  weekly.
- **`.github/workflows/dependency-review.yml`** — fails PRs that add a dependency with a known
  high-severity vulnerability.

`Dependabot` keeps Maven and GitHub Actions dependencies up to date.

### One-time setup: server + repository secrets

The `docker-build` and `deploy` jobs only run on pushes to `master`. Before the first deploy,
the following must be configured once — otherwise those jobs will fail.

**1. Server prerequisites**

The deploy script assumes a plain Linux server with:

- **Docker** installed (`docker`, `docker login`, `docker run` used by the deploy script).
- A **Postgres** instance reachable from the server (connection details go into `.env-prod`).
- Ports **8080** (main app) and **8081** (candidate during blue-green deploy) free and open.

**2. Env file `/cosmo/.env-prod` on the server**

The deploy script runs the container with `--env-file /cosmo/.env-prod`, so this file must exist
and contain at least the production variables from [section 8](#8-environment-variables):

```shell
SPRING_PROFILES_ACTIVE=prod
POSTGRES_URL=jdbc:postgresql://<host>:5432/cosmo_backend
POSTGRES_USERNAME=...
POSTGRES_PASSWORD=...
API_KEYS=key1,key2
API_KEYS_REQUIRED=true
FB_TOKEN=...
FB_PAGE_TOKEN=...        # optional if the token is stored via POST /api/facebook/token
FB_PAGE_ID=...
CORS_ALLOWED_ORIGINS=https://cosmopk.pl
PROD_URL=https://cosmopk.pl
```

The smoke tests read `API_KEYS` (first key) and `FB_TOKEN` directly from this file
(`grep` on `/cosmo/.env-prod`), so make sure both are present. The file is only read on the
server and is never committed to the repository.

**3. GitHub Actions secrets**

Create these under `Settings → Secrets and variables → Actions` of the repository:

| Secret | Used for | Notes |
| --- | --- | --- |
| `DOCKERHUB_USERNAME` | `docker login` (push + pull of the image) | Docker Hub account |
| `DOCKERHUB_TOKEN` | `docker login` | access token, not the account password |
| `SERVER_HOST` | SSH target host of the deploy job | e.g. `cosmo.example.com` |
| `SERVER_USER` | SSH user | must be able to run `docker` commands |
| `SERVER_KEY` | SSH private key for GitHub Actions | see below |

**4. SSH key for GitHub Actions**

The helper script [`scripts/key-config.sh`](./scripts/key-config.sh) generates a dedicated SSH key
pair, installs the public part on the server (`authorized_keys`) and leaves the private key at
`~/.ssh/github-actions`:

```shell
./scripts/key-config.sh <user>@<server-host> your-email@example.com
```

Then paste the **private key** as the `SERVER_KEY` secret. The user must have permission to run
`docker` (e.g. member of the `docker` group) because the deploy script logs into Docker Hub and
manages containers directly.

**5. First deploy**

Everything above in place, simply push to `master`. The pipeline runs
`test → docker-build → deploy`; if the promoted container fails its health check, the previous
image is restarted automatically. To (re)deploy a specific version later, use
`Actions → CI/CD → Run workflow` with a `tag` input (e.g. `sha-<commit>`).

## Observability (bug hunting)

- Every response carries an `X-Request-Id` header, and the same id is put into the logs
  (`requestId` MDC field). When something breaks, take the id from the failing HTTP response and
  `grep` the server logs for it — you get the whole trace of that single request.
- `GET /actuator/info` (exposed in prod) reports the exact **git commit** that is running, so you
  always know which version is deployed.
- In `prod` Swagger UI/OpenAPI docs are disabled and actuator exposes only `health` and `info`.
  Locally (`local` profile) the health endpoint shows full details and Swagger UI is available
  at `/swagger-ui.html`.

## Details

You can find **Facebook API** usage documentation [here](./docs/facebook-api.md)