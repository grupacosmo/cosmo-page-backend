# cosmo-page-backend

## General Information

Contains an implementation for an API service used by cosmo-page infrastructure.

Before deploying the Spring Boot application, ensure that you have the following prerequisites installed and configured on your deployment environment:

![](./docs/high-level-design.png)

![](./docs/detailed-design.png)

## Prerequisites

- Java JDK 20+(Spring Boot 3.1.4+)
- Maven 3.6.0+
- PostgreSQL

## Setup

All setup related operations are processed via **Maven** build system. A Maven **wrapper**
(`mvnw`) is included, so no global Maven installation is required:

```shell
./mvnw clean install       # Linux/macOS
mvnw.cmd clean install     # Windows
```

After the execution of command given above the executable **JAR** file will be generated and
placed into **target** folder in the root directory of the project.

### Local (dev)

The whole stack (Postgres **and** the backend) starts together with a single command and
requires **no environment variables**:

```shell
docker compose up -d
```

Compose starts Postgres first and waits for it to be healthy (`pg_isready`), then starts the
backend. The app connects to Postgres at `jdbc:postgresql://postgres:5432/cosmo_backend`
(`postgres`/`postgres`), uses the `local` profile, the Facebook emulator is enabled, and the
local API key defaults to `local-dev-key`.

On startup the app **auto-seeds** the database with the emulator's sample posts, so there is
**no need to call any endpoint manually** before the API returns data.

The API is exposed on `http://localhost:8080`.

To stop everything: `docker compose down`.

Both services have `restart: unless-stopped` and healthchecks, so Compose brings them back up
automatically and reports their status via `docker compose ps`.

To build/run only the backend manually (e.g. with hot-reload), you can still start just the
database with `docker compose up -d postgres` and run `./mvnw spring-boot:run` on the host —
the `local` profile points at `localhost:5432`.

### Facebook emulator

In the `local` profile the Facebook Graph API is **emulated** (`facebook.emulator.enabled=true`).
Instead of calling the real API, the app serves sample posts (`FacebookClientEmulator`), so you can
develop and test the whole flow — `POST /api/posts/sync`, `GET /api/posts`, `GET /api/facebook/posts`
— **without a real page access token or network access to Facebook**. The database is seeded with
sample posts automatically at startup.

In `prod` (`facebook.emulator.enabled=false`) the real Facebook client is used and `FB_TOKEN`,
`CLIENT_ID` and `CLIENT_SECRET` are required.

### Prod

Prod reads its configuration from environment variables. The only one that is *truly required*
for the app to talk to Facebook is the page access token (`FB_TOKEN`).

Optional file-based setup: copy `.env.example` to `.env` and fill in the values. The
`spring-dotenv` dependency loads `.env` automatically at startup, so no shell exports are needed.

```dotenv
POSTGRES_URL
POSTGRES_USERNAME
POSTGRES_PASSWORD
FB_TOKEN          # webhook verification token
FB_PAGE_TOKEN     # page access token used to fetch posts (alternative to /api/facebook/token)
FB_PAGE_ID        # the Facebook page id
CLIENT_ID         # optional
CLIENT_SECRET     # optional
PROD_URL
API_KEYS
MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD
```

At startup the app reads the page token from the database (set via `POST /api/facebook/token`).
If the database is empty, it falls back to `FB_PAGE_TOKEN` + `FB_PAGE_ID` from the environment —
so you can provide the token purely via env, with no manual API call.

Run the application with the `prod` profile:

```shell
java -jar -Dspring.profiles.active=prod target/cosmo-backend-<version>.jar
```

In `prod` the database is **not** auto-seeded — posts are populated via the Facebook webhook
and/or `POST /api/posts/sync`.

The only difference between dev and prod is the active profile — nothing else needs to change.

## Details

You can find **Facebook API** usage documentation [here](./docs/facebook-api.md)