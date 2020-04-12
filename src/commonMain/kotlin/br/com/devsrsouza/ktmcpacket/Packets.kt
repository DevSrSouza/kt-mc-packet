package br.com.devsrsouza.ktmcpacket

import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import br.com.devsrsouza.ktmcpacket.packets.Packet
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.packets.client.play.LoginStart
import br.com.devsrsouza.ktmcpacket.packets.client.status.Ping
import br.com.devsrsouza.ktmcpacket.packets.client.status.Request
import br.com.devsrsouza.ktmcpacket.packets.server.login.LoginSuccess
import br.com.devsrsouza.ktmcpacket.packets.server.play.*
import br.com.devsrsouza.ktmcpacket.packets.server.status.Pong
import br.com.devsrsouza.ktmcpacket.packets.server.status.ServerListPing
import kotlinx.serialization.KSerializer

typealias PacketId = Int

enum class Packets(
    val client: Map<PacketId, KSerializer<out ClientPacket>>,
    val server: Map<PacketId, KSerializer<out ServerPacket>>
) {
    STATUS(
        client = mapOf(
            0x00 to Request.serializer(),
            0x01 to Ping.serializer()
        ),
        server = mapOf(
            0x00 to ServerListPing.serializer(),
            0x01 to Pong.serializer()
        )
    ),
    LOGIN(
        client = mapOf(
            0x00 to LoginStart.serializer()
        ),
        server = mapOf(
            0x02 to LoginSuccess.serializer()
        )
    ),
    PLAY(
        client = mapOf(),
        server = mapOf(
            0x00 to SpawnEntity.serializer(),
            0x01 to SpawnExperienceOrb.serializer(),
            0x02 to SpawnWeatherEntity.serializer(),
            0x03 to SpawnLivingEntity.serializer(),
            0x05 to SpawnPlayer.serializer(),
            0x06 to EntityAnimation.serializer(),
            0x08 to AcknowledgePlayerDigging.serializer(),
            0x09 to BlockBreakAnimation.serializer(),
            0x26 to JoinGame.serializer(),
            0x36 to PlayerPositionAndLook.serializer()
        )
    );

    companion object {
        val HANDSHAKE = Handshake.serializer()
    }
}

fun <T : ServerPacket> Packets.findServerPacketById(
    id: PacketId
): KSerializer<T>? = server[id] as KSerializer<T>?

fun <T : ClientPacket> Packets.findClientPacketById(
    id: PacketId
): KSerializer<T>? = client[id] as KSerializer<T>?

fun Packets.findIdByPacket(
    serializer: KSerializer<Packet>
): Int? = findIdByServerPacket(serializer) ?: findIdByClientPacket(serializer)

fun Packets.findIdByServerPacket(
    serializer: KSerializer<Packet>
): Int? = server.findId(serializer)

fun Packets.findIdByClientPacket(
    serializer: KSerializer<Packet>
): Int? = client.findId(serializer)

private fun Map<Int, KSerializer<out Packet>>.findId(
    serializer: KSerializer<Packet>
): Int? = entries.find {
    it.value.descriptor.serialName == serializer.descriptor.serialName
}?.key