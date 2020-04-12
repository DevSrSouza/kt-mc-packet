package br.com.devsrsouza.ktmcpacket.packets.client.play

import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import kotlinx.serialization.Serializable

// 0x00
// FIXME: Migrate to Login package in next Major release.
@Serializable
data class LoginStart(
        @MinecraftString(16)
        val nickname: String
) : ClientPacket
