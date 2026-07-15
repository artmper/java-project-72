import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("org.sonarqube") version "7.3.1.8318"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("gg.jte.gradle") version "3.1.9"
    id("io.freefair.lombok") version "8.13.1"
    jacoco
    application
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("gg.jte:jte:3.2.4")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.konghq:unirest-java-core:4.10.0")
    implementation("org.jsoup:jsoup:1.21.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")

    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("hexlet.code.App")
}

jte {
    contentType.set(gg.jte.ContentType.Html)
    sourceDirectory.set(file("src/main/resources/templates/jte").toPath())
    generate()
}

sonar {
    properties {
        property("sonar.projectKey", "artmper_java-project-72")
        property("sonar.organization", "artmper")
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
    }
}


tasks.test {
    useJUnitPlatform()
    // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
    dependsOn(tasks.test)
}

tasks.named("sonar") {
    dependsOn(tasks.jacocoTestReport)
}

tasks.shadowJar {
    mergeServiceFiles()
}
