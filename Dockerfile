FROM gradle:8.12.1-jdk21

WORKDIR /java-project-72

COPY . .

WORKDIR /java-project-72/app

RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /java-project-72/app/build/libs/app-1.0-SNAPSHOT-all.jar app.jar

CMD ["java", "-jar", "app.jar"]