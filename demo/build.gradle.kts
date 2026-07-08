plugins {
    id("java")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation(libs.minestom)
    implementation(libs.adventure.minimessage)

    implementation(libs.jetbrains.annotations)
    // logging
    implementation(libs.logback.classic)
    implementation(libs.logback.encoder)
}

java{
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

application {
    mainClass.set("net.minestom.demo.Main")
}