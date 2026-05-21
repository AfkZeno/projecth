
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.backend"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.backend.MainKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    implementation(libs.h2database.h2)
    implementation(libs.h2database.r2dbc)
    implementation(libs.koin.ktor)
    implementation(libs.koin.loggerSlf4j)
    implementation(libs.logback.classic)

    implementation(libs.postgre)

    implementation(libs.koin.logger)
    implementation(libs.datetime)
    implementation(libs.bcrypt)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}

tasks{
    shadowJar{

        mergeServiceFiles()
        mergeServiceFiles("META-INF/services")

        relocate("io.r2dbc", "shadow.io.r2dbc")
        relocate("org.postgresql", "shadow.org.postgresql")

        manifest{
            attributes["Main-Class"] = "com.backend.MainKt"
        }
        archiveBaseName.set("ProjectHBackend")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
    }
}

