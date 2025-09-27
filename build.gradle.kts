import com.github.spotbugs.snom.SpotBugsTask

plugins {
    id("checkstyle")
    id("com.github.spotbugs") version "6.2.2"
    `java-library`
}

group = "net.bridgesplash.sidebar"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("net.minestom:minestom:2025.09.13-1.21.8")
    api("net.kyori:adventure-text-minimessage:4.24.0")

    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.3")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0")

    // lombok
    implementation("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // jetbrains annotations
    implementation("org.jetbrains:annotations:26.0.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.kyori:adventure-text-minimessage:4.24.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
    toolVersion = "10.14.0"
    maxWarnings = 0
}

configurations.checkstyle {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
        select("com.google.guava:guava:23.0")
    }
}

tasks{
    withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

    compileJava {
        options.encoding = "UTF-8"
    }

    withType<Checkstyle>().configureEach {
        reports {
            xml.required = false
            html.required = true
        }
    }

    withType<SpotBugsTask>().configureEach {
        reports.create("html") {
            required = true
        }
        reports.create("xml") {
            required = false
        }
    }

    test {
        useJUnitPlatform()
    }
}