package br.com.devsrsouza.ktmcpacket.utils

import br.com.devsrsouza.ktmcpacket.exceptions.MinecraftProtocolDecodingException
import br.com.devsrsouza.ktmcpacket.exceptions.MinecraftProtocolEncodingException
import br.com.devsrsouza.ktmcpacket.utils.MinecraftVarIntEncoder.readVarInt
import br.com.devsrsouza.ktmcpacket.utils.MinecraftVarIntEncoder.writeVarInt
import io.ktor.utils.io.core.*

object MinecraftStringEncoder {
    const val MINECRAFT_MAX_STRING_LENGTH = 32767

    @ExperimentalStdlibApi
    inline fun readString(
        maxLength: Int,
        readByte: () -> Byte,
        readBytes: (length: Int) -> ByteArray,
    ): String {
        val length: Int = readVarInt(readByte)
        return if (length > maxLength * 4) {
            throw MinecraftProtocolDecodingException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
        } else if (length < 0) {
            throw MinecraftProtocolDecodingException("The received encoded string buffer length is less than zero! Weird string!")
        } else {
            val stringBuffer = readBytes(length).decodeToString()
            if (stringBuffer.length > maxLength) {
                throw MinecraftProtocolDecodingException("The received string length is longer than maximum allowed ($length > $maxLength)")
            } else {
                stringBuffer
            }
        }
    }

    inline fun writeString(
        string: String,
        writeByte: (Byte) -> Unit,
        writeFully: (ByteArray) -> Unit
    ) {
        val bytes = string.toByteArray()

        if (bytes.size > MINECRAFT_MAX_STRING_LENGTH) {
            throw MinecraftProtocolEncodingException("String too big (was " + bytes.size + " bytes encoded, max " + 32767 + ")")
        } else {
            writeVarInt(bytes.size, writeByte)
            writeFully(bytes)
        }
    }
}

