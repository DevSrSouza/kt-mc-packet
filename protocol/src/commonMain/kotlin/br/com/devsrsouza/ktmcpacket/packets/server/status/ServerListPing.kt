package br.com.devsrsouza.ktmcpacket.packets.server.status

import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import kotlinx.serialization.Serializable

@Serializable
data class ServerListPing(
        val jsonResponse: String
) : ServerPacket