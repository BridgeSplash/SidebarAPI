plugins {
    id("java")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")

    implementation("org.jetbrains:annotations:26.0.2")
    // logging
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
}

java{
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

application {
    mainClass.set("net.minestom.demo.Main")
}