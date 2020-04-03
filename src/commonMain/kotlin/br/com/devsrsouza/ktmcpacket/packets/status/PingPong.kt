package br.com.devsrsouza.ktmcpacket.packets.status

import kotlinx.serialization.Serializable

object PingPongPacket {
    @Serializable
    data class Client(
        val payload: Long
    )

    @Serializable
    data class Server(
        val payload: Long
    )
}