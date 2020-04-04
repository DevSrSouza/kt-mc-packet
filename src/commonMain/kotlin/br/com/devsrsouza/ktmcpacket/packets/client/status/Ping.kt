package br.com.devsrsouza.ktmcpacket.packets.client.status

import kotlinx.serialization.Serializable

@Serializable
data class Ping(
    val payload: Long
)