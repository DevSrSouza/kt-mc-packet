package gradle.plugin.configuration

import gradle.plugin.configuration.default.applyDefaultPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyDefaultPlugins()
    }
}
