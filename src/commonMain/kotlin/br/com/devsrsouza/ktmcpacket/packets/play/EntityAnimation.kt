package br.com.devsrsouza.ktmcpacket.packets.play

import br.com.devsrsouza.ktmcpacket.MinecraftEnum
import br.com.devsrsouza.ktmcpacket.MinecraftEnumType.BYTE
import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import kotlinx.serialization.Serializable

object EntityAnimationPacket {

    @Serializable
    @MinecraftEnum(BYTE)
    enum class Animation {
        SWING_MAIN_ARM,
        TAKE_DAMAGE,
        LEAVE_BED,
        SWING_OFFHAND,
        CRITICAL_EFFECT,
        MAGIC_CRITICAL_EFFECT
    }

    @Serializable
    data class Server(
        @MinecraftNumber(VAR)
        val entityId: Int,

        val animation: Animation // Unsigned Byte
    )
}