package br.com.devsrsouza.ktmcpacket.packets.client.play

import br.com.devsrsouza.ktmcpacket.MinecraftString
import kotlinx.serialization.Serializable

// 0x00
@Serializable
data class LoginStart(
        @MinecraftString(16)
        val nickname: String
)
