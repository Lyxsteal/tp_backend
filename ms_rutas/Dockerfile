# --- ETAPA 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build

# Copiar POM raíz y de los módulos para cache de dependencias
COPY pom.xml .
COPY api-gateway/pom.xml ./api-gateway/
COPY ms_solicitud/pom.xml ./ms_solicitud/
COPY ms_rutas/pom.xml ./ms_rutas/

# Descargar dependencias (cache)
RUN mvn dependency:go-offline

# Copiar código fuente de todos los módulos
COPY api-gateway/src ./api-gateway/src
COPY ms_solicitud/src ./ms_solicitud/src
COPY ms_rutas/src ./ms_rutas/src

# Compilar SOLO el módulo ms_rutas y sus dependencias
RUN mvn -DskipTests -pl ./ms_rutas -am clean package spring-boot:repackage

# --- ETAPA 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Variable del JAR generado
ARG JAR_FILE=/build/ms_rutas/target/ms_rutas-0.0.1-SNAPSHOT.jar

# Copiar JAR construido
COPY --from=build ${JAR_FILE} app.jar

# Exponer el puerto del microservicio rutas
EXPOSE 8085

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

