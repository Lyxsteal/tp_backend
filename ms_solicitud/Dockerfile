# Etapa 1: Construir la aplicación
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copiar todos los pom.xml para cachear las dependencias
COPY pom.xml .
COPY api-gateway/pom.xml ./api-gateway/
COPY ms_solicitud/pom.xml ./ms_solicitud/
COPY ms_rutas/pom.xml ./ms_rutas/

# Descargar dependencias (esta capa se cachea)
RUN mvn -pl ./ms_solicitud -am dependency:go-offline

# Copiar el código fuente
COPY ms_solicitud/src/ ./ms_solicitud/src/

# Construir la aplicación usando las dependencias cacheadas
RUN mvn -pl ./ms_solicitud -am -DskipTests clean package spring-boot:repackage

# Etapa 2: Crear la imagen final
FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_NAME=ms_solicitud-0.0.1-SNAPSHOT.jar
COPY --from=build /build/ms_solicitud/target/${JAR_NAME} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
