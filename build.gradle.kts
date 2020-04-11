plugins {
    kotlin("multiplatform") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
    id("maven-publish")
}

group = "br.com.devsrsouza"
version = "0.1.0"

repositories {
    jcenter()
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    jvm()
    js()

    val serialization_version = "0.20.0"
    val kotlinx_io_version = "0.1.16"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serialization_version")
                implementation("io.ktor:ktor-io-native:1.3.2")
                implementation("com.benasher44:uuid:0.1.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {}
    }
}