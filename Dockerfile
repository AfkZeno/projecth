FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar archivos de Gradle primero (mejora el caché)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Dar permisos de ejecución
RUN chmod +x gradlew

# Descargar dependencias (esto se cachea)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY . .

# Construir con ShadowJar
RUN ./gradlew clean shadowJar --no-daemon

# ====================== Runtime Stage ======================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR generado por ShadowJar
# Normalmente se llama: build/libs/*.jar (el que termina en -all.jar o similar)
COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]