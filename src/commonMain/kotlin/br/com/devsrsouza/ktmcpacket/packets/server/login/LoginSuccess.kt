package br.com.devsrsouza.ktmcpacket.packets.server.login

import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.types.UUIDStringSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

// 0x02
@Serializable
data class LoginSuccess(
        @Serializable(with = UUIDStringSerializer::class)
        val uuid: Uuid,

        @MinecraftString(16)
        val nickname: String
)