import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Instant

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
    id("com.google.cloud.tools.jib") version "3.1.4"
}

val postgresVersion = "42.5.1"
val assertJVersion = "3.24.2"

group = "ro.jf.bk"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.flywaydb:flyway-core:9.19.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers-bom:1.18.3")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mockserver")
    testImplementation("org.mock-server:mockserver-client-java:5.15.0")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "openjdk:17-jdk-slim"
    }
    to {
        image = "bookkeeper/account-service"
    }
    container {
        mainClass = "ro.jf.bk.BookkeeperApplicationKt"
        creationTime = Instant.now().toString()
    }
}