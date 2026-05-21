FROM gradle:8.9-jdk21 AS build

WORKDIR /app


COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY . .
RUN chmod +x gradlew

RUN ./gradlew wrapper --gradle-version 8.11.1

RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]