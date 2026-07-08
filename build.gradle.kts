import com.github.spotbugs.snom.SpotBugsTask

plugins {
    id("checkstyle")
    alias(libs.plugins.spotbugs)
    `java-library`
    `maven-publish`
}

group = "net.bridgesplash.sidebar"
version = System.getenv("TAG_VERSION")?: "dev"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.minestom)
    api(libs.adventure.minimessage)

    compileOnly(libs.slf4j)

    compileOnly(libs.spotbugs.annotations)
    spotbugsPlugins(libs.spotbugs.findsecbugs.plugin)

    // lombok
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)

    // jetbrains annotations
    implementation(libs.jetbrains.annotations)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.adventure.minimessage)
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

    toolchain.languageVersion = JavaLanguageVersion.of(25)
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
            url = uri("https://repo.tesseract.club/releases")
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
