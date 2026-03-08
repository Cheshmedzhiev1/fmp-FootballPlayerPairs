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
    implementation(project(":application")) // needed so the persistence adapter can implement the driven ports like
    // (PlayerDrivenPort,RecordDrivenPort etc.)
    implementation(project(":domain")) // needed for mapping between JPA entities and domain models like (Player, Record, Match etc)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
    implementation("org.postgresql:postgresql:42.7.1")
    //brings everything that we need to talk to our database
    // Hibernate (the KPA implementation, does the actual SQL)
    // Spring Data JPA
    // transaction manager
    // all jpa annotations (Entity,Table, Column, Id etc.)
}


//needs domain       (to map to/from domain models)
//needs application  (to implement driven ports)
// needs jpa          (to talk to the database)
//  needs postgresql   (to connect to Postgres specifically)