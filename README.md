# cosmo-page-backend

## General Information

Contains an implementation for an API service used by cosmo-page infrastructure.

Before deploying the Spring Boot application, ensure that you have the following prerequisites installed and configured on your deployment environment:

## Prerequisites

- Java JDK 20+(Spring Boot 3.1.4+)
- Maven 3.6.0+
- PostgreSQL

## Setup

All setup related operations are processed via **Maven** build system.

In order to perform build operation it's required to execute the following command:

```shell
mvn clean install
```

After the execution of command given above the executable **JAR** file will be generated and placed into **target** folder in the root directory of the project

### Local

In order to deploy this application locally, you will have to use **Docker** environment:

Local deployment requires a setup of the next environment variables inside **docker-compose.yaml** file:

```dotenv
CLIENT_ID
CLIENT_SECRET
```

In order to start local deployment it's required to execute the following command:

```shell
docker-compose up -d
```

In this case **application-local.yml** configuration file would be used by default.

### Prod

In order to deploy this application in a prod environment, you will have to use previously generated **JAR** file:

Prod deployment requires a setup of the next environment variables inside **docker-compose.yaml** file:

```dotenv
POSTGRES_USERNAME
POSTGRES_PASSWORD
POSTGRES_URL
CLIENT_ID
CLIENT_SECRET
FB_TOKEN
PROD_URL
```

In this case **application-prod.yml** configuration file should be used by default.

In order to run the application it's required to execute the following command:

```shell
java -jar -Dspring.profiles.active=prod target/cosmo-backend-<version>.jar
```

## Details

You can find **Facebook API** usage documentation [here](./docs/facebook-api.md)