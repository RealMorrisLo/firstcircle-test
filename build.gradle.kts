buildscript {
    val kotlinVersion by extra("2.3.0")
    val ktorVersion by extra("2.3.11")
    
    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}

plugins {
    `java-library`
    application
    kotlin("jvm")
}

group = "com.firstcircle"
version = "1.0.0"

repositories {
    mavenCentral()
}

val kotlinVersion = "2.3.0"
val ktorVersion = "2.3.11"

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", kotlinVersion))
    
    // Ktor server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    // Testing
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

application {
    mainClass.set("com.firstcircle.ApplicationKt")
}

kotlin {
    jvmToolchain(24)
}