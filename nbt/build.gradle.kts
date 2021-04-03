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
    }
}
