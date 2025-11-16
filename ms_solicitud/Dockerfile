# --- ETAPA 1: Construcción (Build) ---
# Usar Java 21 para compilar
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build

# 1. Copia el POM raíz
COPY pom.xml .

# 2. Copia los POM de TODOS los módulos
COPY api-gateway/pom.xml ./api-gateway/
COPY ms_solicitud/pom.xml ./ms_solicitud/
COPY ms_rutas/pom.xml ./ms_rutas/

# 3. Descarga las dependencias de TODO el proyecto
RUN mvn dependency:go-offline

# 4. Copia el CÓDIGO FUENTE de todos los módulos
COPY api-gateway/src ./api-gateway/src
COPY ms_solicitud/src ./ms_solicitud/src
COPY ms_rutas/src ./ms_rutas/src

# 5. Compila SOLO este módulo y sus dependencias
#    (Reemplazá <nombre-del-modulo> por: api-gateway, ms_solicitud, o ms_rutas)
RUN mvn -DskipTests -pl ./ms_solicitud -am clean package spring-boot:repackage

# ----------------------------------------------------
# ETAPA 2: LEVANTAMIENTO/EJECUCIÓN (Runtime Stage)
# ----------------------------------------------------
# ¡USA JAVA 21 PARA EJECUTAR!
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 6. Define la ruta al JAR
ARG JAR_FILE=/build/ms_solicitud/target/ms_solicitud-0.0.1-SNAPSHOT.jar

# 7. Copia el JAR final de la etapa de build
COPY --from=build ${JAR_FILE} app.jar

# 8. Expone el puerto
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]