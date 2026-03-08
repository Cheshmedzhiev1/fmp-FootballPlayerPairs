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
    implementation(project(":application")) // needed so controllers can call the driving ports (ForPlayer etc.)
    implementation(project(":domain")) //needed so the controllers can use domain models (Player,Match etc.)
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.5") // adds validation support
    // brings everything needed for REST (Tomcat, Spring MVC, Jackson)
}
