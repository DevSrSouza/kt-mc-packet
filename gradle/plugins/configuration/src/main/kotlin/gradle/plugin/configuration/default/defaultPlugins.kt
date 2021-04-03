package gradle.plugin.configuration.default

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import gradle.plugin.configuration.utils.*
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jmailen.gradle.kotlinter.tasks.LintTask

internal fun Project.applyDefaultPlugins() {
    apply(plugin = "org.jmailen.kotlinter")
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    configureSupportedTargets()
    configureKotlinter()
    configureTestLogger()
}

private fun Project.configureKotlinter() {
    tasks {
        withType(LintTask::class.java) {
            exclude("**/generated/**")
        }
    }
}

private fun Project.configureTestLogger() {
    extensions.configure<TestLoggerExtension> {
        theme = ThemeType.MOCHA_PARALLEL
    }
}

private fun Project.configureSupportedTargets() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm()
        js()

        // TODO: add native
    }
}