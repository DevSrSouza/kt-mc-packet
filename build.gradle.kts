plugins {
    kotlin("multiplatform") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
    id("maven-publish")
}

group = "br.com.devsrsouza"
version = "0.3.0-SNAPSHOT"

repositories {
    jcenter()
}

kotlin {
    jvm()
    js()

    val serialization_version = "1.1.0"
    val ktor_version = "1.5.2"
    val benasher_uuid_version = "0.2.3"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serialization_version")
                implementation("io.ktor:ktor-io:$ktor_version")
                implementation("com.benasher44:uuid:$benasher_uuid_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.ktor:ktor-network:$ktor_version")
            }
        }

        val jvmTest by getting {}
    }
}