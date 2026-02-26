pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.21"
        kotlin("plugin.serialization") version "2.0.21"
        id("io.gitlab.arturbosch.detekt") version "1.23.6"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "first-circle-engineering-test"
