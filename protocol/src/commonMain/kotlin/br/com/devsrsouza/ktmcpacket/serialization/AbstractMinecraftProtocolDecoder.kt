package br.com.devsrsouza.ktmcpacket.serialization

import br.com.devsrsouza.ktmcpacket.utils.minecraft
import io.ktor.utils.io.core.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.internal.TaggedDecoder

@OptIn(InternalSerializationApi::class)
abstract class AbstractMinecraftProtocolDecoder(
    protected val input: Input
) : TaggedDecoder<ProtocolDesc>() {
    fun decodeUByte() = input.readUByte()
    fun decodeUShort() = input.readUShort()
    fun decodeUInt() = input.readUInt()
    fun decodeULong() = input.readULong()

    fun decodeVarInt() = input.minecraft.readVarInt()
}