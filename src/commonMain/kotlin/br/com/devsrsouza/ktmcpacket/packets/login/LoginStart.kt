package br.com.devsrsouza.ktmcpacket.packets.login

import br.com.devsrsouza.ktmcpacket.MinecraftString
import kotlinx.serialization.Serializable

object LoginStartPacket {
    // 0x00
    @Serializable
    data class Client(
        @MinecraftString(16)
        val nickname: String
    )
}