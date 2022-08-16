import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.openrewrite.java-library")
    id("org.openrewrite.maven-publish")
    id("org.openrewrite.shadow")
}

repositories {
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases-local/")
    }
}

dependencies {
    api(project(":rewrite-groovy"))
    compileOnly(project(":rewrite-test"))
    compileOnly(platform(kotlin("bom")))
    compileOnly(kotlin("stdlib"))
    implementation(project(":rewrite-properties"))

    runtimeOnly("org.codehaus.groovy:groovy-ant:latest.release")
    runtimeOnly("org.codehaus.groovy:groovy-xml:latest.release")

    compileOnly("org.gradle:gradle-base-services:latest.release")
    compileOnly("org.gradle:gradle-core-api:latest.release")
    compileOnly("org.gradle:gradle-language-groovy:latest.release")
    compileOnly("org.gradle:gradle-language-java:latest.release")
    compileOnly("org.gradle:gradle-logging:latest.release")
    compileOnly("org.gradle:gradle-messaging:latest.release")
    compileOnly("org.gradle:gradle-native:latest.release")
    compileOnly("org.gradle:gradle-process-services:latest.release")
    compileOnly("org.gradle:gradle-resources:latest.release")
    compileOnly("org.gradle:gradle-testing-base:latest.release")
    compileOnly("org.gradle:gradle-testing-jvm:latest.release")

    testImplementation(project(":rewrite-test")) {
        // because gradle-api fatjars this implementation already
        exclude("ch.qos.logback", "logback-classic")
    }
    testImplementation(project(":rewrite-maven"))

    testRuntimeOnly("org.gradle:gradle-base-services:latest.release")
    testRuntimeOnly("org.gradle:gradle-core-api:latest.release")
    testRuntimeOnly("org.gradle:gradle-language-groovy:latest.release")
    testRuntimeOnly("org.gradle:gradle-language-java:latest.release")
    testRuntimeOnly("org.gradle:gradle-logging:latest.release")
    testRuntimeOnly("org.gradle:gradle-messaging:latest.release")
    testRuntimeOnly("org.gradle:gradle-native:latest.release")
    testRuntimeOnly("org.gradle:gradle-process-services:latest.release")
    testRuntimeOnly("org.gradle:gradle-resources:latest.release")
    testRuntimeOnly("org.gradle:gradle-testing-base:latest.release")
    testRuntimeOnly("org.gradle:gradle-testing-jvm:latest.release")
    testRuntimeOnly(project(":rewrite-java-11"))
}

tasks.withType<ShadowJar> {
    exclude("org/slf4j/impl/**.class")
    exclude("org/gradle/internal/logging/slf4j/**.class")
    dependencies {
        include(dependency("org.gradle:"))
    }
}
