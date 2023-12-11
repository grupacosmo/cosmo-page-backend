FROM maven:3.9.5-amazoncorretto-21 AS build

WORKDIR /project

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /project/src

RUN mvn package

FROM amazoncorretto:20-alpine-jdk

RUN mkdir /app

RUN addgroup -g 1001 -S cosmo

RUN adduser -S cosmopk -u 1001

COPY --from=build /project/target/cosmo-backend-0.0.1-SNAPSHOT.jar /app/backend.jar

WORKDIR /app

RUN chown -R cosmopk:cosmo /app

EXPOSE 8000

CMD java $JAVA_OPTS -jar backend.jar
