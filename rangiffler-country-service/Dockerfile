FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the prebuilt fat JAR
COPY build/libs/rangiffler-country-service-all.jar app.jar

EXPOSE 4001
ENTRYPOINT ["java", "-jar", "app.jar"]