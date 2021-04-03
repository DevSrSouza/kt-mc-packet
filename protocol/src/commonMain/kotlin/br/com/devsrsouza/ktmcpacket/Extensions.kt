package br.com.devsrsouza.ktmcpacket

import br.com.devsrsouza.ktmcpacket.exceptions.PacketNotFoundAtStateException
import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import br.com.devsrsouza.ktmcpacket.packets.Packet
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.utils.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerializationStrategy

// FIXME migrate to sealed interface in Kotlin 1.5 and add length to it
sealed class PacketContent<T : Packet> {
    data class Found<T : Packet>(
        val state: PacketState,
        val type: PacketType<T>,
        val length: Int,
        val packet: T
    ) : PacketContent<T>()

    data class NotFound<T : Packet>(
        val length: Int,
        val id: Int,
        val remaningContent: ByteArray,
    ) : PacketContent<T>()
}

fun MinecraftByteInput.readPacket(
    state: PacketState,
    isServer: Boolean,
): PacketContent<Packet> {
    return readPacket(
        state,
        isServer,
        { readVarInt() },
        { byteArray -> buffer.readFully(byteArray) },
    )
}

suspend fun MinecraftByteReadChannel.readPacket(
    state: PacketState,
    isServer: Boolean,
): PacketContent<Packet>  {
    return readPacket(
        state,
        isServer,
        { readVarInt() },
        { byteArray -> channel.readFully(byteArray) },
    )
}

fun MinecraftByteInput.readServerPacket(
    state: PacketState,
): PacketContent<ServerPacket> {
    return readPacket(
        state = state,
        isServer = true,
    )  as PacketContent<ServerPacket>
}

fun MinecraftByteInput.readClientPacket(
    state: PacketState,
): PacketContent<ClientPacket> {
    return readPacket(
        state = state,
        isServer = false,
    )  as PacketContent<ClientPacket>
}

suspend fun MinecraftByteReadChannel.readServerPacket(
    state: PacketState,
): PacketContent<ServerPacket> {
    return readPacket(
        state = state,
        isServer = true,
    )  as PacketContent<ServerPacket>
}

suspend fun MinecraftByteReadChannel.readClientPacket(
    state: PacketState,
): PacketContent<ClientPacket> {
    return readPacket(
        state = state,
        isServer = false,
    )  as PacketContent<ClientPacket>
}

fun <T : Packet> MinecraftByteOutput.writePacket(
    state: PacketState,
    value: T,
) {
    writePacket(
        state,
        value,
        { varint -> writeVarInt(varint) },
        { bytes -> buffer.writeFully(bytes) }
    )
}

suspend fun <T : Packet> MinecraftByteWriteChannel.writePacket(
    state: PacketState,
    value: T,
    flushing: Boolean = true,
) {
    writePacket(
        state,
        value,
        { varint -> writeVarInt(varint) },
        { bytes -> channel.writeFully(bytes) }
    )
    if(flushing) channel.flush()
}

inline fun readPacket(
    state: PacketState,
    isServer: Boolean,
    readVarInt: () -> Int,
    readFullyTo: (ByteArray) -> Unit,
): PacketContent<Packet> {
    val length = readVarInt()
    val id = readVarInt()

    // this here is required to not instantiate a new ByteArray/ByteReadPacket
    // to memory just for reading the ID and after read the rest of the stream
    val idVarIntBytesCount = MinecraftVarIntEncoder.varIntBytesCount(id)

    val payload = ByteArray(length - idVarIntBytesCount)
    readFullyTo(payload)

    val packetType = when(isServer) {
        true -> state.serverById[id]
        false -> state.clientById[id]
    } ?: return PacketContent.NotFound(
        length = length,
        id = id,
        remaningContent = payload
    )

    val packet = MinecraftProtocol.decodeFromByteArray(
        packetType.serializer,
        payload
    )

    return PacketContent.Found<Packet>(
        state = state,
        type = packetType as PacketType<Packet>,
        length = length,
        packet = packet,
    )
}

inline fun <T : Packet> writePacket(
    state: PacketState,
    value: T,
    writeVarInt: (Int) -> Unit,
    writeFully: (ByteArray) -> Unit
) {
    val type = state.byKClass[value::class] ?: throw PacketNotFoundAtStateException()
    val idVarIntBytesCount = MinecraftVarIntEncoder.varIntBytesCount(type.id)

    val packet = MinecraftProtocol.encodeToByteArray(
        serializer = type.serializer as SerializationStrategy<T>,
        value = value
    )

    // the idVarIntBytesCount is usage here to prevent memory allocation of a Buffer
    // to write the packet id and the packet payload and then get the final length
    // here we avoid this.
    val length = packet.size + idVarIntBytesCount

    writeVarInt(length)
    writeVarInt(type.id)
    writeFully(packet)
}

