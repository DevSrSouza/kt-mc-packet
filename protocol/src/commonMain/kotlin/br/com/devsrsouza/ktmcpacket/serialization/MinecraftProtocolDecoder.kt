package br.com.devsrsouza.ktmcpacket.serialization

import br.com.devsrsouza.ktmcpacket.MinecraftEnumType
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.exceptions.MinecraftProtocolDecodingException
import br.com.devsrsouza.ktmcpacket.utils.MinecraftStringEncoder.MINECRAFT_MAX_STRING_LENGTH
import br.com.devsrsouza.ktmcpacket.utils.minecraft
import io.ktor.utils.io.core.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder

@InternalSerializationApi
open class MinecraftProtocolDecoder(
    input: Input
) : AbstractMinecraftProtocolDecoder(input) {
    private var currentIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return if (descriptor.elementsCount == currentIndex)
            DECODE_DONE
        else currentIndex++
    }

    override fun decodeTaggedBoolean(
        tag: ProtocolDesc
    ): Boolean = when (val i = input.readByte()) {
        0x00.toByte() -> false
        0x01.toByte() -> true
        else -> throw MinecraftProtocolDecodingException("Expected boolean value (0 or 1), found $i")
    }

    override fun decodeTaggedByte(
        tag: ProtocolDesc
    ): Byte = when (tag.type) {
        MinecraftNumberType.UNSIGNED -> decodeUByte().toByte()
        else -> input.readByte()
    }

    override fun decodeTaggedShort(
        tag: ProtocolDesc
    ): Short = when (tag.type) {
        MinecraftNumberType.UNSIGNED -> decodeUShort().toShort()
        else -> input.readShort()
    }

    override fun decodeTaggedInt(
        tag: ProtocolDesc
    ): Int = when (tag.type) {
        MinecraftNumberType.DEFAULT -> input.readInt()
        MinecraftNumberType.UNSIGNED -> decodeUInt().toInt()
        MinecraftNumberType.VAR -> decodeVarInt()
    }

    override fun decodeTaggedLong(
        tag: ProtocolDesc
    ): Long = when (tag.type) { // TODO: impl VarLong?
        MinecraftNumberType.UNSIGNED -> decodeULong().toLong()
        else -> input.readLong()
    }

    override fun decodeTaggedFloat(
        tag: ProtocolDesc
    ): Float = input.readFloat()

    override fun decodeTaggedDouble(
        tag: ProtocolDesc
    ): Double = input.readDouble()

    @ExperimentalStdlibApi
    override fun decodeTaggedString(
        tag: ProtocolDesc
    ): String = input.minecraft.readString(MINECRAFT_MAX_STRING_LENGTH)

    @ExperimentalStdlibApi
    override fun decodeTaggedEnum(
        tag: ProtocolDesc, enumDescription: SerialDescriptor
    ): Int {
        val enumTag = extractEnumParameters(enumDescription)
        val ordinal = when (enumTag.type) {
            MinecraftEnumType.VARINT -> decodeVarInt()
            MinecraftEnumType.BYTE -> input.readByte().toInt()
            MinecraftEnumType.UNSIGNED_BYTE -> decodeUByte().toInt()
            MinecraftEnumType.INT -> input.readInt()
            MinecraftEnumType.STRING ->
                enumDescription.getElementIndex(input.minecraft.readString(enumTag.stringMaxLength))
        }

        return findEnumIndexByTag(enumDescription, ordinal)
    }

    override fun decodeTaggedInline(tag: ProtocolDesc, inlineDescriptor: SerialDescriptor): Decoder {
        return super.decodeTaggedInline(tag, inlineDescriptor)
    }

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        currentTag.type
        return super.decodeCollectionSize(descriptor)
    }

    override fun SerialDescriptor.getTag(index: Int) =
        extractParameters(this, index)

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeDecoder {
        return when (descriptor.kind) {
            is StructureKind.CLASS -> {
                // TODO
                return MinecraftProtocolDecoder(input)
            }
            is StructureKind.LIST -> {
                // TODO
                super.beginStructure(descriptor)
            }
            else -> {
                super.beginStructure(descriptor)
            }
        }
    }
}
