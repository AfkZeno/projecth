# ====================== Build Stage ======================
FROM gradle:8.11-jdk21 AS build

WORKDIR /app

# Copiar archivos de Gradle
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Dar permisos
RUN chmod +x gradlew

# Copiar el resto del proyecto
COPY . .

# Construir con ShadowJar
RUN ./gradlew clean shadowJar --no-daemon

# ====================== Runtime Stage ======================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR (usamos * para que agarre cualquier nombre)
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]