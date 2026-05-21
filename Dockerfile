# ====================== Build Stage ======================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar archivos de Gradle primero (para cachear dependencias)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Dar permisos
RUN chmod +x gradlew

# Descargar dependencias (esto se cachea si no cambian)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY . .

# Construir el Fat JAR
RUN ./gradlew clean buildFatJar --no-daemon

# ====================== Runtime Stage ======================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar solo el JAR generado
COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080

# Render usa automáticamente la variable $PORT
CMD ["java", "-jar", "app.jar"]