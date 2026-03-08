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
    implementation(project(":domain"))  // brings core spring framework, minimal spring dependency, not full spring boot
    implementation("org.springframework:spring-context:6.1.4")
}


// no spring boot, no JPA, no web/rest dependencies
// this module only needs to know about the domain and basic spring wiring.