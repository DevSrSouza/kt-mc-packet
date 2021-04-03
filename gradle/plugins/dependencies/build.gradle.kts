plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "br.com.devsrsouza.dependencies"
version = "0.0.1"

repositories {
    jcenter()
}

gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "gradle.plugin.dependencies.DependenciesPlugin"
    }
}