plugins {
    java
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"

    id("com.diffplug.spotless") version "6.25.0"
}

group = "br.com.project"
version = "0.0.1-SNAPSHOT"
description = "hydro-flow"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy {
        force("org.eclipse.jdt:org.eclipse.jdt.core:3.35.0")
        force("org.eclipse.jdt:ecj:3.35.0")
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
//        googleJavaFormat("1.35.0")
        palantirJavaFormat("2.89.0")
        target("src/**/*.java")
        targetExclude("**/generated/**")
    }
}

tasks.register("formatJava") {
    group = "formatting"
    description = "Formata todos os arquivos Java Palantir Formatter"
    dependsOn("spotlessApply")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    // Source: https://mvnrepository.com/artifact/org.liquibase/liquibase-core
    implementation("org.liquibase:liquibase-core:5.0.1")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-liquibase
    implementation("org.springframework.boot:spring-boot-starter-liquibase:4.0.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
