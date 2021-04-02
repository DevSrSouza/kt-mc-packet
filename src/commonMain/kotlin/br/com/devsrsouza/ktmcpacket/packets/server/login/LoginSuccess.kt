package br.com.devsrsouza.ktmcpacket.packets.server.login

import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.UuidStringSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

// 0x02
@Serializable
data class LoginSuccess(
        @Serializable(with = UuidStringSerializer::class)
        val uuid: Uuid,

        @MinecraftString(16)
        val nickname: String
) : ServerPacket