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


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.ktxSerializationCore)
                implementation(Dependencies.ktxSerializationProtoBuf)
                implementation(Dependencies.ktorIO)
                implementation(Dependencies.benasher44Uuid)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Dependencies.ktorNetwork)
            }
        }

        val jvmTest by getting {}
    }
}