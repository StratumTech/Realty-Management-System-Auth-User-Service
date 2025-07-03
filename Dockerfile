FROM eclipse-temurin:17 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./

COPY gradle ./gradle

COPY src ./src

RUN chmod +x gradlew \
    && ./gradlew clean bootJar -x test

FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /app/build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]