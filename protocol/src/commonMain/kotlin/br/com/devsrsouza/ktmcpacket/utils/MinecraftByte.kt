package br.com.devsrsouza.ktmcpacket.utils

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

val Input.minecraft get() = MinecraftByteInput(this)
val Output.minecraft get() = MinecraftByteOutput(this)

val ByteReadChannel.minecraft get() = MinecraftByteReadChannel(this)
val ByteWriteChannel.minecraft get() = MinecraftByteWriteChannel(this)

inline class MinecraftByteInput(val buffer: Input) {
    @ExperimentalStdlibApi
    fun readString(maxLength: Int): String {
        return MinecraftStringEncoder.readString(
            maxLength,
            buffer::readByte,
            buffer::readBytes,
        )
    }

    fun readVarInt(): Int {
        return MinecraftVarIntEncoder.readVarInt(
            buffer::readByte,
        )
    }
}

inline class MinecraftByteOutput(val buffer: Output) {
    fun writeString(string: String) {
        MinecraftStringEncoder.writeString(
            string,
            buffer::writeByte,
            buffer::writeFully,
        )
    }

    fun writeVarInt(value: Int) {
        MinecraftVarIntEncoder.writeVarInt(
            value,
            buffer::writeByte,
        )
    }
}

inline class MinecraftByteReadChannel(val channel: ByteReadChannel) {
    @ExperimentalStdlibApi
    suspend fun readString(maxLength: Int): String {
        return MinecraftStringEncoder.readString(
            maxLength,
            { channel.readByte() },
            { length -> ByteArray(length).also { channel.readFully(it, 0, length) } },
        )
    }

    suspend fun readVarInt(): Int {
        return MinecraftVarIntEncoder.readVarInt(
            { channel.readByte() }
        )
    }
}

inline class MinecraftByteWriteChannel(val channel: ByteWriteChannel) {
    suspend fun writeString(string: String) {
        MinecraftStringEncoder.writeString(
            string,
            { byte -> channel.writeByte(byte) },
            { bytes -> channel.writeFully(bytes) },
        )
    }

    suspend fun writeVarInt(value: Int) {
        MinecraftVarIntEncoder.writeVarInt(
            value,
            { byte -> channel.writeByte(byte) },
        )
    }
}