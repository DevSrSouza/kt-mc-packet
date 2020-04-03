package br.com.devsrsouza.ktmcpacket.internal

import io.ktor.utils.io.core.*
import kotlin.experimental.and
import kotlin.experimental.or

class MinecraftProtocolEncodingException(message: String) : RuntimeException(message)
class MinecraftProtocolDecodingException(message: String) : RuntimeException(message)

const val MINECRAFT_MAX_STRING_LENGTH = 32767

val Input.minecraft get() = MinecraftByteInput(this)
val Output.minecraft get() = MinecraftByteOutput(this)

inline class MinecraftByteInput(val buffer: Input) {

    inline fun <reified T : Enum<T>> readEnumValue(): T {
        val ordinal = readVarInt()
        return enumValues<T>().find { it.ordinal == ordinal } as T
    }

    @ExperimentalStdlibApi
    fun readString(maxLength: Int): String {
        val i: Int = readVarInt()
        return if (i > maxLength * 4) {
            throw MinecraftProtocolDecodingException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")")
        } else if (i < 0) {
            throw MinecraftProtocolDecodingException("The received encoded string buffer length is less than zero! Weird string!")
        } else {
            val stringBuffer = buffer.readBytes(i).decodeToString()
            if (stringBuffer.length > maxLength) {
                throw MinecraftProtocolDecodingException("The received string length is longer than maximum allowed ($i > $maxLength)")
            } else {
                stringBuffer
            }
        }
    }

    fun readVarInt(): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = buffer.readByte()
            val value = (read and 127).toInt()
            result = result or (value shl 7 * numRead)
            numRead++
            if (numRead > 5) {
                throw RuntimeException("VarInt is too big")
            }
        } while (read and 128.toByte() != 0.toByte())
        return result
    }
}

inline class MinecraftByteOutput(val buffer: Output) {
    fun writeEnumValue(value: Enum<*>) {
        writeVarInt(value.ordinal)
    }

    fun writeString(string: String) {
        val bytes = string.toByteArray()

        if (bytes.size > MINECRAFT_MAX_STRING_LENGTH) {
            throw MinecraftProtocolEncodingException("String too big (was " + bytes.size + " bytes encoded, max " + 32767 + ")")
        } else {
            writeVarInt(bytes.size)
            buffer.writeFully(bytes)
        }
    }

    fun writeVarInt(value: Int) {
        var value = value
        do {
            var temp = (value and 127).toByte()
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value = value ushr 7
            if (value != 0) {
                temp = temp or 128.toByte()
            }
            buffer.writeByte(temp)
        } while (value != 0)
    }
}