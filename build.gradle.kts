import com.github.spotbugs.snom.SpotBugsTask

plugins {
    id("checkstyle")
    id("com.github.spotbugs") version "6.2.2"
    `java-library`
    `maven-publish`
}

group = "net.bridgesplash.sidebar"
version = System.getenv("TAG_VERSION")?: "dev"

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

java{
    withSourcesJar()
    withJavadocJar()

    toolchain.languageVersion = JavaLanguageVersion.of(21)
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

publishing{

    repositories{
        maven {
            name = "release"
            url = uri("https://repo.tesseract.club/private")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }

    publications{
        create<MavenPublication>("maven"){
            groupId = "net.bridgesplash"
            artifactId = "sidebar-api"
            version = project.version as String
            from(components["java"])

            pom{
                name.set(project.name)
                description.set("A sidebar library that feels uses state to handle updates")
                url.set("https://github.com/BridgeSplash/SidebarAPI")

                developers{
                    developer {
                        id.set("tropicalshadow")
                        name.set("TropicalShadow")
                        email.set("me@tesseract.club")
                    }
                }

                issueManagement{
                    system.set("GitHub")
                    url.set("https://github.com/BridgeSplash/SidebarAPI/issues")
                }

                scm{
                    connection.set("scm:git:git://github.com/BridgeSplash/SidebarAPI.git")
                    developerConnection.set("scm:git:ssh://github.com/BridgeSplash/SidebarAPI.git")
                    url.set("https://github.com/BridgeSplash/SidebarAPI")
                    tag.set("HEAD")
                }

                ciManagement {
                    system.set("Github Actions")
                    url.set("https://github.com/BridgeSplash/SidebarAPI/actions")
                }
            }

        }
    }
}
