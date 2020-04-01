package br.com.devsrsouza.ktmcpacket.packets

import br.com.devsrsouza.ktmcpacket.MinecraftEnum
import br.com.devsrsouza.ktmcpacket.MinecraftEnumType.*
import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.*
import br.com.devsrsouza.ktmcpacket.SerialOrdinal
import kotlinx.serialization.Serializable

object HandshakePacket {
    @Serializable
    @MinecraftEnum(VARINT)
    enum class HandshakeNextState {
        @SerialOrdinal(1) STATUS,
        @SerialOrdinal(2) LOGIN
    }

    @Serializable
    data class Client(
        @MinecraftNumber(VAR)
        val version: Int,

        val address: String,

        @MinecraftNumber(UNSIGNED)
        val port: Short,

        val nextState: HandshakeNextState
    )
}

