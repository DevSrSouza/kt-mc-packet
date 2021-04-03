plugins {
    id("module")
}


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Dependencies.ktxSerializationCore)
                api(Dependencies.ktorIO)
                api(Dependencies.benasher44Uuid)
            }
        }
    }
}

