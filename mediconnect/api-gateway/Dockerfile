FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/api-gateway-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx128m", "-Xms64m", "-jar", "app.jar"]
