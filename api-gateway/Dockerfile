FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml ./pom.xml
COPY api-gateway/ ./api-gateway
COPY ms_solicitud/pom.xml ms_solicitud/pom.xml
COPY ms_rutas/pom.xml     ms_rutas/pom.xml
RUN mvn -DskipTests -pl ./api-gateway -am clean package spring-boot:repackage

FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_NAME=api-gateway-0.0.1-SNAPSHOT.jar
COPY --from=build /build/api-gateway/target/${JAR_NAME} app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
