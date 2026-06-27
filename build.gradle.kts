plugins {
    java

    jacoco
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"

    id("com.diffplug.spotless") version "6.25.0"

    id("com.adarshr.test-logger") version "4.0.0"

    id("com.google.cloud.tools.jib") version "3.5.1"
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

jib {
    from {
        image = "eclipse-temurin:25-jdk"
    }

    container {
        environment = mapOf(
            "HOME" to "/tmp"
        )

        ports = listOf("8080")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Remova as duas linhas do Jackson e deixe só essa
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.instancio:instancio-junit:5.4.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required = true
        xml.required = true
    }
}

tasks.test {
    reports {
        junitXml.required = true
        html.required = true
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs("-XX:+EnableDynamicAgentLoading")

    outputs.upToDateWhen { false }
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
    showExceptions = true
    showStackTraces = true
    showCauses = true
    slowThreshold = 1000
    showSummary = true
    showPassed = true
    showSkipped = true
    showFailed = true
}

tasks.bootRun {
    dependsOn(tasks.named("test"))
}
