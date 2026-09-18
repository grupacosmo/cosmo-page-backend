FROM maven:3.9.5-amazoncorretto-21 AS build

ARG GIT_COMMIT=unknown

WORKDIR /project

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /project/src

RUN mvn package -Dmaven.test.skip=true && \
    printf 'git.commit.id=%s\ngit.commit.id.abbrev=%s\n' "$GIT_COMMIT" "$(printf '%s' "$GIT_COMMIT" | cut -c1-7)" >> target/classes/git.properties

FROM amazoncorretto:21-alpine-jdk

RUN mkdir /app

RUN addgroup -g 1001 -S cosmo

RUN adduser -S -G cosmo -u 1001 cosmopk

ARG JAR_FILE=target/cosmo-backend-0.0.1-SNAPSHOT.jar

COPY --from=build /project/$JAR_FILE /app/backend.jar

WORKDIR /app

RUN chown -R cosmopk:cosmo /app

USER cosmopk

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar backend.jar"]
