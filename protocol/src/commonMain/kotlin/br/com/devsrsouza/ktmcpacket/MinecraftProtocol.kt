package br.com.devsrsouza.ktmcpacket

import br.com.devsrsouza.ktmcpacket.serialization.MinecraftProtocolDecoder
import br.com.devsrsouza.ktmcpacket.serialization.MinecraftProtocolEncoder
import io.ktor.utils.io.core.*
import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class MinecraftProtocol(
    override val serializersModule: SerializersModule = EmptySerializersModule
) : BinaryFormat {

    companion object Default : BinaryFormat by MinecraftProtocol()

    @InternalSerializationApi
    override fun <T> encodeToByteArray(
            serializer: SerializationStrategy<T>,
            value: T
    ): ByteArray {
        val packetBuilder = BytePacketBuilder()

        encodeToByteArray(packetBuilder, serializer, value)

        return packetBuilder.build().readBytes()
    }

    @InternalSerializationApi
    fun <T> encodeToByteArray(
        output: Output,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val encoder = MinecraftProtocolEncoder(output)

        encoder.encodeSerializableValue(serializer, value)
    }

    @InternalSerializationApi
    override fun <T> decodeFromByteArray(
            deserializer: DeserializationStrategy<T>,
            bytes: ByteArray
    ): T {
        val packetRead = ByteReadPacket(bytes)

        return decodeFromByteArray(deserializer, packetRead)
    }

    @InternalSerializationApi
    fun <T> decodeFromByteArray(
        deserializer: DeserializationStrategy<T>,
        input: Input
    ): T {
        val decoder = MinecraftProtocolDecoder(input)
        return decoder.decodeSerializableValue(deserializer)
    }
}

