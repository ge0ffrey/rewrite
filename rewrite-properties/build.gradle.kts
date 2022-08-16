plugins {
    id("org.openrewrite.java-library")
    id("org.openrewrite.maven-publish")
}

dependencies {
    api(project(":rewrite-core"))
    api("org.jetbrains:annotations:latest.release")
    api("com.fasterxml.jackson.core:jackson-annotations:latest.release")

    compileOnly(project(":rewrite-test"))
    compileOnly(platform(kotlin("bom")))
    compileOnly(kotlin("stdlib"))

    implementation("io.micrometer:micrometer-core:1.9.+")

    testImplementation(project(":rewrite-test"))
}
