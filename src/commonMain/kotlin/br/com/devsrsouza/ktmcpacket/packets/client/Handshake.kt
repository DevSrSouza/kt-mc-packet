package br.com.devsrsouza.ktmcpacket.packets.client

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.SerialOrdinal
import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import kotlinx.serialization.Serializable

@Serializable
enum class HandshakeNextState {
    @SerialOrdinal(1) STATUS,
    @SerialOrdinal(2) LOGIN
}

@Serializable
data class Handshake(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val version: Int,

    @MinecraftString(255)
    val address: String,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val port: Short,

    val nextState: HandshakeNextState
) : ClientPacket