plugins {
    id("java")
}

group = "com.football"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral() // tells gradle where to download dependencies from
}

dependencies {  // j unit available during testing, not included in final build
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {  // tells gradle to use junit 5 to discover and run tests
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}