package br.com.devsrsouza.ktmcpacket.packets.server.status

import kotlinx.serialization.Serializable

@Serializable
data class Pong(
        val payload: Long
)