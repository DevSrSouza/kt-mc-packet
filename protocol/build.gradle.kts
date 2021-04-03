plugins {
    id("module")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                api(Dependencies.ktorNetwork)
            }

        }
    }
}
