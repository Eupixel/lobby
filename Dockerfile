FROM gradle:8.4-jdk21 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src
RUN chmod +x gradlew
RUN ./gradlew clean build
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 25565
ENTRYPOINT ["java","-jar","app.jar"]