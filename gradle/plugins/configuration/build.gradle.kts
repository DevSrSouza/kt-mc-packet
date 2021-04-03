plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("dependencies")
}

group = "br.com.devsrsouza.configuration"
version = "0.0.1"

repositories {
    google()
    jcenter()
    gradlePluginPortal()
}

dependencies {
    implementation(BuildPlugins.kotlin)
    implementation(BuildPlugins.ktlint)
    implementation(BuildPlugins.ktxSerialization)
    implementation(BuildPlugins.testLogger)
}

kotlin.sourceSets.getByName("main").kotlin.srcDir("../dependencies/src/main/kotlin")

gradlePlugin {
    plugins.register("module") {
        id = "module"
        implementationClass = "gradle.plugin.configuration.ModulePlugin"
    }
}