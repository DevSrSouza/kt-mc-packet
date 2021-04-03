package gradle.plugin.configuration.utils

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

inline fun <reified T> T.logger(): Logger = Logging.getLogger(T::class.java)

inline fun <reified T> T.info(message: String) = logger().log(LogLevel.INFO, message)
inline fun <reified T> T.error(message: String) = logger().log(LogLevel.ERROR, message)
inline fun <reified T> T.lifecycle(message: String) = logger().log(LogLevel.LIFECYCLE, message)
