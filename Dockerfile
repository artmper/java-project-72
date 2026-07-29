FROM gradle:8.12.1-jdk21

WORKDIR /java-project-72

COPY . .

WORKDIR /java-project-72/app

RUN ./gradlew shadowJar --no-daemon

CMD ["java", "-jar", "build/libs/app-1.0-SNAPSHOT-all.jar"]