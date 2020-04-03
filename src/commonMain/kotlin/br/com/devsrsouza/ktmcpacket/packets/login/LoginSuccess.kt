package br.com.devsrsouza.ktmcpacket.packets.login

import br.com.devsrsouza.ktmcpacket.MinecraftString
import br.com.devsrsouza.ktmcpacket.types.UUIDStringSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

object LoginSuccessPacket {
    // 0x02
    @Serializable
    data class Server(
        @Serializable(with = UUIDStringSerializer::class)
        val uuid: Uuid,

        @MinecraftString(16)
        val nickname: String
    )
}