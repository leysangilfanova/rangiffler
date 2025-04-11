FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the prebuilt fat JAR
COPY /build/libs/rangiffler-auth-2.4.5.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]