# --- ETAPA 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# 1. Copiar el POM raíz
COPY pom.xml .

# 2. Copiar los POM de TODOS los módulos para que Maven resuelva dependencias
COPY api-gateway/pom.xml ./api-gateway/
COPY ms_solicitud/pom.xml ./ms_solicitud/
COPY ms_rutas/pom.xml ./ms_rutas/

# 3. Copiar TODO el código fuente (Maven lo necesita para el flag -am)
COPY api-gateway/src ./api-gateway/src
COPY ms_solicitud/src ./ms_solicitud/src
COPY ms_rutas/src ./ms_rutas/src

# 4. Compilar y empaquetar
RUN mvn -DskipTests -pl ./api-gateway -am clean package spring-boot:repackage

# --- ETAPA 2: Ejecución (Run) ---
FROM eclipse-temurin:21-jdk
WORKDIR /app

# 5. Definir el nombre del JAR
ARG JAR_NAME=api-gateway-0.0.1-SNAPSHOT.jar

# 6. Copiar solo el JAR final y limpio
COPY --from=build /build/api-gateway/target/${JAR_NAME} app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]