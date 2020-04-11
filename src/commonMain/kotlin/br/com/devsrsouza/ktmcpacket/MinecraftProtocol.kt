package br.com.devsrsouza.ktmcpacket

import br.com.devsrsouza.ktmcpacket.internal.*
import br.com.devsrsouza.ktmcpacket.internal.extractEnumElementParameters
import br.com.devsrsouza.ktmcpacket.internal.extractEnumParameters
import br.com.devsrsouza.ktmcpacket.internal.extractParameters
import br.com.devsrsouza.ktmcpacket.internal.findEnumIndexByTag
import io.ktor.utils.io.core.*
import kotlinx.serialization.*
import kotlinx.serialization.CompositeDecoder.Companion.READ_DONE
import kotlinx.serialization.internal.TaggedDecoder
import kotlinx.serialization.internal.TaggedEncoder
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule

class MinecraftProtocol(
    override val context: SerialModule = EmptyModule
) : BinaryFormat {

    companion object Default : BinaryFormat by MinecraftProtocol()

    @InternalSerializationApi
    override fun <T> dump(
            serializer: SerializationStrategy<T>,
            value: T
    ): ByteArray {
        val packetBuilder = BytePacketBuilder()
        val encoder = MinecraftProtocolEncoder(packetBuilder)

        encoder.encode(serializer, value)

        return packetBuilder.build().readBytes()
    }

    @InternalSerializationApi
    override fun <T> load(
            deserializer: DeserializationStrategy<T>,
            bytes: ByteArray
    ): T {
        val packetRead = ByteReadPacket(bytes)

        val decoder = MinecraftProtocolDecoder(packetRead)
        return decoder.decode(deserializer)
    }

    @InternalSerializationApi
    private open class MinecraftProtocolEncoder(
            val output: Output
    ) : TaggedEncoder<ProtocolDesc>() {

        override fun shouldEncodeElementDefault(
                descriptor: SerialDescriptor, index: Int
        ): Boolean = true

        override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
            return extractParameters(this, index)
        }

        override fun encodeTaggedInt(
                tag: ProtocolDesc, value: Int
        ) {
            when(tag.type) {
                MinecraftNumberType.DEFAULT -> output.writeInt(value)
                MinecraftNumberType.UNSIGNED -> output.writeUInt(value.toUInt())
                MinecraftNumberType.VAR -> output.minecraft.writeVarInt(value)
            }
        }
        override fun encodeTaggedByte(
            tag: ProtocolDesc, value: Byte
        ) {
            when(tag.type) {
                MinecraftNumberType.UNSIGNED -> output.writeUByte(value.toUByte())
                else -> output.writeByte(value)
            }
        }
        override fun encodeTaggedShort(
            tag: ProtocolDesc, value: Short
        ) {
            when(tag.type) {
                MinecraftNumberType.UNSIGNED -> output.writeUShort(value.toUShort())
                else -> output.writeShort(value)
            }
        }
        override fun encodeTaggedLong(
            tag: ProtocolDesc, value: Long
        ) {
            // TODO: impl VarLong?
            when(tag.type) {
                MinecraftNumberType.UNSIGNED -> output.writeULong(value.toULong())
                else -> output.writeLong(value)
            }
        }
        override fun encodeTaggedFloat(
            tag: ProtocolDesc, value: Float
        ) = output.writeFloat(value)
        override fun encodeTaggedDouble(
            tag: ProtocolDesc, value: Double
        ) = output.writeDouble(value)
        override fun encodeTaggedBoolean(
            tag: ProtocolDesc, value: Boolean
        ) = output.writeByte(if(value) 0x01 else 0x00)
        override fun encodeTaggedString(
            tag: ProtocolDesc, value: String
        ) = output.minecraft.writeString(value)
        override fun encodeTaggedEnum(
                tag: ProtocolDesc,
                enumDescription: SerialDescriptor,
                ordinal: Int
        ) {
            val enumTag = extractEnumElementParameters(
                enumDescription,
                ordinal
            )
            when (extractEnumParameters(enumDescription).type) {
                MinecraftEnumType.VARINT -> output.minecraft.writeVarInt(enumTag.ordinal)
                MinecraftEnumType.BYTE -> output.writeByte(enumTag.ordinal.toByte())
                MinecraftEnumType.UNSIGNED_BYTE -> output.writeUByte(enumTag.ordinal.toUByte())
                MinecraftEnumType.INT -> output.writeInt(enumTag.ordinal)
                MinecraftEnumType.STRING ->
                    output.minecraft.writeString(enumDescription.getElementName(ordinal))
            }
        }

        override fun beginStructure(
            descriptor: SerialDescriptor,
            vararg typeSerializers: KSerializer<*>
        ): CompositeEncoder {
            return when(descriptor.kind) {
                is PrimitiveKind -> RepeatedMinecraftProtocolEncoder(
                    output,
                    ProtocolDesc(MinecraftNumberType.DEFAULT, MINECRAFT_MAX_STRING_LENGTH)
                )
                else -> super.beginStructure(descriptor, *typeSerializers)
            }
        }
    }

    @InternalSerializationApi
    private class RepeatedMinecraftProtocolEncoder(
        output: Output,
        val tag: ProtocolDesc
    ) : MinecraftProtocolEncoder(output) {
        override fun SerialDescriptor.getTag(index: Int) = tag
    }

    @InternalSerializationApi
    private open class MinecraftProtocolDecoder(
        val input: Input
    ) : TaggedDecoder<ProtocolDesc>() {
        private var currentIndex = 0

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            return if(descriptor.elementsCount == currentIndex)
                READ_DONE
            else currentIndex++
        }

        override fun decodeTaggedBoolean(
            tag: ProtocolDesc
        ): Boolean = when(val i = input.readByte()) {
            0x00.toByte() -> false
            0x01.toByte() -> true
            else -> throw MinecraftProtocolDecodingException("Expected boolean value (0 or 1), found $i")
        }

        override fun decodeTaggedByte(
            tag: ProtocolDesc
        ): Byte = when(tag.type) {
            MinecraftNumberType.UNSIGNED -> input.readUByte().toByte()
            else -> input.readByte()
        }
        override fun decodeTaggedShort(
            tag: ProtocolDesc
        ): Short = when(tag.type) {
            MinecraftNumberType.UNSIGNED -> input.readUShort().toShort()
            else -> input.readShort()
        }
        override fun decodeTaggedInt(
            tag: ProtocolDesc
        ): Int = when(tag.type) {
            MinecraftNumberType.DEFAULT -> input.readInt()
            MinecraftNumberType.UNSIGNED -> input.readUInt().toInt()
            MinecraftNumberType.VAR -> input.minecraft.readVarInt()
        }
        override fun decodeTaggedLong(
            tag: ProtocolDesc
        ): Long = when (tag.type) { // TODO: impl VarLong?
            MinecraftNumberType.UNSIGNED -> input.readULong().toLong()
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
                MinecraftEnumType.VARINT -> input.minecraft.readVarInt()
                MinecraftEnumType.BYTE -> input.readByte().toInt()
                MinecraftEnumType.UNSIGNED_BYTE -> input.readUByte().toByte().toInt()
                MinecraftEnumType.INT -> input.readInt()
                MinecraftEnumType.STRING ->
                    enumDescription.getElementIndex(input.minecraft.readString(enumTag.stringMaxLength))
            }

            return findEnumIndexByTag(enumDescription, ordinal)
        }

        override fun SerialDescriptor.getTag(index: Int) =
            extractParameters(this, index)

        override fun beginStructure(
            descriptor: SerialDescriptor,
            vararg typeParams: KSerializer<*>
        ): CompositeDecoder {
            return when(descriptor.kind) {
                is PrimitiveKind -> RepeatedMinecraftProtocolDecoder(
                    input,
                    ProtocolDesc(MinecraftNumberType.DEFAULT, MINECRAFT_MAX_STRING_LENGTH)
                )
                else -> super.beginStructure(descriptor, *typeParams)
            }
        }
    }

    @InternalSerializationApi
    private class RepeatedMinecraftProtocolDecoder(
        input: Input,
        val tag: ProtocolDesc
    ) : MinecraftProtocolDecoder(input) {
        override fun SerialDescriptor.getTag(index: Int) = tag
    }
}

internal data class ProtocolDesc(
    val type: MinecraftNumberType,
    val stringMaxLength: Int
)

internal data class ProtocolEnumDesc(
    val type: MinecraftEnumType,
    val stringMaxLength: Int
)

internal data class ProtocolEnumElementDesc(
    val ordinal: Int
)