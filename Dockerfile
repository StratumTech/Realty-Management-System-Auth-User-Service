FROM eclipse-temurin:17 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew && ./gradlew clean bootJar -x test --stacktrace && \
    ls -la /app/build/libs/ || (echo "JAR file not found in /app/build/libs/" && exit 1)

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/build/libs/com.stratumtech-user-service-0.0.1-SNAPSHOT.jar app.jar
RUN ls -la || (echo "app.jar not copied to /app/" && exit 1)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]