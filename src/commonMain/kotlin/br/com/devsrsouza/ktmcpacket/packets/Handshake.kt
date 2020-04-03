package br.com.devsrsouza.ktmcpacket.packets

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.*
import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.SerialOrdinal
import kotlinx.serialization.Serializable

object HandshakePacket {
    @Serializable
    enum class HandshakeNextState {
        @SerialOrdinal(1) STATUS,
        @SerialOrdinal(2) LOGIN
    }

    @Serializable
    data class Client(
        @MinecraftNumber(VAR)
        val version: Int,

        @MinecraftString(255)
        val address: String,

        @MinecraftNumber(UNSIGNED)
        val port: Short,

        val nextState: HandshakeNextState
    )
}

