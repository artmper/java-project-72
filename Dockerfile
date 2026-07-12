FROM gradle:8.12.1-jdk21

WORKDIR /app

COPY . .

RUN ./gradlew shadowJar --no-daemon

CMD ["java", "-jar", "app/build/libs/app-1.0-SNAPSHOT-all.jar"]