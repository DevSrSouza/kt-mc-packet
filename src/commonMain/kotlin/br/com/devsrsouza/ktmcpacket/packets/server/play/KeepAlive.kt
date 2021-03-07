package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import kotlinx.serialization.Serializable

// 0x1F
@Serializable
data class ServerKeepAlive(
    val keepAliveId: Long
) : ServerPacket