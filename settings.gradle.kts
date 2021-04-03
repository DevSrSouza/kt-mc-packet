rootProject.name = "kt-mc-packet"

enableFeaturePreview("GRADLE_METADATA")

fun includePlugin(vararg pluginPaths: String) =
    pluginPaths.forEach { path -> includeBuild("gradle/plugins/$path") }

includePlugin("dependencies", "configuration")

include("core")
include("protocol")
include("nbt")