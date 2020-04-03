package br.com.devsrsouza.ktmcpacket.internal

import br.com.devsrsouza.ktmcpacket.types.UUIDSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerialModuleCollector
import kotlin.reflect.KClass

object MinecraftModule : SerialModule {
    override fun dumpTo(collector: SerialModuleCollector) {}

    override fun <T : Any> getContextual(
        kclass: KClass<T>
    ): KSerializer<T>? {
        return when(kclass) {
            Uuid::class -> UUIDSerializer as KSerializer<T>
            else -> null
        }
    }

    override fun <T : Any> getPolymorphic(
        baseClass: KClass<T>,
        value: T
    ): KSerializer<out T>? = null

    override fun <T : Any> getPolymorphic(
        baseClass: KClass<T>,
        serializedClassName: String
    ): KSerializer<out T>? = null
}