FROM openjdk:21-slim
WORKDIR /app
ARG JAR_NAME=gateway-0.0.1-SNAPSHOT.jar
COPY target/${JAR_NAME} app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]