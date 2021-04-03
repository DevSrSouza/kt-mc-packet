package br.com.devsrsouza.ktmcpacket.packets.client.status

import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import kotlinx.serialization.Serializable

@Serializable
data class Ping(
    val payload: Long
) : ClientPacket