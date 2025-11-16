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