FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/notification-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-Xmx128m", "-Xms64m", "-jar", "app.jar"]