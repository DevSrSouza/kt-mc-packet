package br.com.devsrsouza.ktmcpacket.packets.client.play

import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import kotlinx.serialization.Serializable

// 0x10
@Serializable
data class ClientKeepAlive(
    val keepAliveId: Long
) : ClientPacket