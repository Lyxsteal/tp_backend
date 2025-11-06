FROM openjdk:21-slim
WORKDIR /app
COPY target/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
