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
    mavenCentral()
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
    implementation("org.postgresql:postgresql:42.7.1")
}
