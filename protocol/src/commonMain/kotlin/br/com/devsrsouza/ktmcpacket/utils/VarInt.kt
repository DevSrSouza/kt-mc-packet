package br.com.devsrsouza.ktmcpacket.utils

import kotlin.experimental.and
import kotlin.experimental.or

object MinecraftVarIntEncoder {
    inline fun readVarInt(
        readByte: () -> Byte,
    ): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = readByte()
            val value = (read and 127).toInt()
            result = result or (value shl 7 * numRead)
            numRead++
            if (numRead > 5) {
                throw RuntimeException("VarInt is too big")
            }
        } while (read and 128.toByte() != 0.toByte())
        return result
    }

    inline fun writeVarInt(
        value: Int,
        writeByte: (Byte) -> Unit,
    ) {
        var value = value
        do {
            var temp = (value and 127).toByte()
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value = value ushr 7
            if (value != 0) {
                temp = temp or 128.toByte()
            }
            writeByte(temp)
        } while (value != 0)
    }

    fun varIntBytesCount(
        value: Int,
    ): Int {
        var counter = 0
        writeVarInt(value) { counter++ }
        return counter
    }
}
