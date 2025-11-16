<<<<<<< HEAD
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build
=======
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app
>>>>>>> f581931c1e0854a69fad4d91b4014d970ada910f

# 1. Copia el archivo POM primero para aprovechar el caché de Docker
COPY pom.xml .

# 2. Descarga las dependencias (solo si el pom.xml ha cambiado)
RUN mvn dependency:go-offline

# 3. Copia el resto de los archivos (código fuente)
COPY src ./src

# 4. Compila la aplicación y genera el JAR final
# -DskipTests asegura que no se ejecuten los tests, acelerando el build.
RUN mvn package -DskipTests

# ----------------------------------------------------
# ETAPA 2: LEVANTAMIENTO/EJECUCIÓN (Runtime Stage)
# Usa una imagen base mínima con solo el JRE para ejecutar el JAR.
# ----------------------------------------------------
<<<<<<< HEAD
FROM eclipse-temurin:21-jre-alpine
=======
FROM eclipse-temurin:17-jre-alpine
>>>>>>> f581931c1e0854a69fad4d91b4014d970ada910f

# Establece el directorio de trabajo
WORKDIR /app

# **IMPORTANTE:** El nombre del JAR se asume como "gateway-0.0.1-SNAPSHOT.jar"
# (basado en el archivo subido). Si cambia, debes modificar esta línea:
ARG JAR_FILE=target/gateway-0.0.1-SNAPSHOT.jar

# Copia el JAR compilado de la etapa 'build' a la etapa de ejecución
COPY --from=build /app/${JAR_FILE} app.jar

# Expone el puerto (el puerto por defecto de Spring Boot)
EXPOSE 8083

# Comando para ejecutar la aplicación
# (La opción -Djava.security.egd acelera el inicio de Spring Boot)
ENTRYPOINT ["java", "-jar", "app.jar"]