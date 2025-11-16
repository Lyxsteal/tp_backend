# --- ETAPA 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build

# Copiamos el pom raíz y los de cada módulo para cache de dependencias
COPY pom.xml .
COPY api-gateway/pom.xml ./api-gateway/
COPY ms_solicitud/pom.xml ./ms_solicitud/
COPY ms_rutas/pom.xml ./ms_rutas/

# Descargamos dependencias offline (se cachea a menos que cambien los POM)
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY api-gateway/src ./api-gateway/src
COPY ms_solicitud/src ./ms_solicitud/src
COPY ms_rutas/src ./ms_rutas/src

# Compilamos SOLO el módulo ms_solicitud junto con sus dependencias
RUN mvn -DskipTests -pl ./ms_solicitud -am clean package spring-boot:repackage

# --- ETAPA 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Variable del JAR generado
ARG JAR_FILE=/build/ms_solicitud/target/ms_solicitud-0.0.1-SNAPSHOT.jar

# Copiamos el JAR desde la etapa anterior
COPY --from=build ${JAR_FILE} app.jar

# Exponemos el puerto del microservicio
EXPOSE 8081

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
