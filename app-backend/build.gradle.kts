plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass.set("Application")
    applicationName = "awesome"
}

repositories {
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
        )
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.komok.tech.to.be.injected)

    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.kaml)

    implementation(libs.flyway.postgresql)
    implementation(libs.jooq)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)

    implementation(libs.bcrypt)

    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.prometheus)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.caching.headers)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.xml)

    implementation(libs.logback)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}
