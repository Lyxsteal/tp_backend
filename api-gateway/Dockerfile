<<<<<<< HEAD
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
RUN mvn -DskipTests -pl ./api-gateway -am clean package spring-boot:repackage

# ----------------------------------------------------
# ETAPA 2: LEVANTAMIENTO/EJECUCIÓN (Runtime Stage)
# ----------------------------------------------------
# ¡USA JAVA 21 PARA EJECUTAR!
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 6. Define la ruta al JAR
ARG JAR_FILE=/build/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar

# 7. Copia el JAR final de la etapa de build
COPY --from=build ${JAR_FILE} app.jar

# 8. Expone el puerto
EXPOSE 8083

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
=======
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# 1. Copia todos los archivos de tu proyecto Gateway
# Dado que el contexto de Docker es la carpeta 'api-gateway/',
# este comando copia pom.xml, src/, etc. al directorio /app del contenedor.
COPY api-gateway/pom.xml .
# Copiamos la carpeta src/ del Gateway
COPY api-gateway/src ./src

# 2. Compila la aplicación y genera el JAR final
# Esto generará el JAR ejecutable en el directorio target/ dentro del contenedor.
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# ETAPA 2: LEVANTAMIENTO/EJECUCIÓN (Runtime Stage)
# Usa una imagen base mínima con solo el JRE para ejecutar el JAR.
# ----------------------------------------------------
# Usamos la versión Alpine de JRE 21 para la imagen más pequeña posible
FROM eclipse-temurin:21-jre-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Define el nombre del JAR generado (basado en el estándar de Spring Boot)
ARG JAR_FILE=target/api-gateway-0.0.1-SNAPSHOT.jar

# Copia el JAR compilado de la etapa 'build' a la etapa de ejecución
COPY --from=build /app/${JAR_FILE} app.jar

# Expone el puerto del Gateway
EXPOSE 8080

# Comando para ejecutar la aplicación
# -Djava.security.egd es una práctica recomendada para Spring Boot en contenedores
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
>>>>>>> f581931c1e0854a69fad4d91b4014d970ada910f
