package br.com.devsrsouza.ktmcpacket.internal

import io.ktor.utils.io.core.*
import kotlin.experimental.and

class MinecraftProtocolEncodingException(message: String) : RuntimeException(message)
class MinecraftProtocolDecodingException(message: String) : RuntimeException(message)

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
        var i = 0
        var j = 0

        while (true) {
            val b0: Byte = buffer.readByte()
            i = i or (b0.and(127).toInt()) shl j++ * 7

            if (j > 5) {
                throw RuntimeException("VarInt too big")
            }

            if ((b0.and(128.toByte())).toInt() != 128) {
                break
            }
        }

        return i
    }
}

inline class MinecraftByteOutput(val buffer: Output) {
    fun writeEnumValue(value: Enum<*>) {
        writeVarInt(value.ordinal)
    }

    fun writeString(string: String) {
        val bytes = string.toByteArray()

        if (bytes.size > 32767) {
            throw MinecraftProtocolEncodingException("String too big (was " + bytes.size + " bytes encoded, max " + 32767 + ")")
        } else {
            writeVarInt(bytes.size)
            buffer.writeFully(bytes)
        }
    }

    fun writeVarInt(`in`: Int) {
        var input = `in`

        while (input and -128 != 0) {
            buffer.writeByte((input.and(127) or 128).toByte())
            input = input ushr 7
        }

        buffer.writeByte(input.toByte())
    }
}