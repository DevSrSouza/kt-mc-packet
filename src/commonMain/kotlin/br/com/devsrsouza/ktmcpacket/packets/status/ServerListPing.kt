package br.com.devsrsouza.ktmcpacket.packets.status

import kotlinx.serialization.Serializable

object ServerListPingPacket {
    @Serializable
    data class Server(
        val jsonResponse: String
    )
}