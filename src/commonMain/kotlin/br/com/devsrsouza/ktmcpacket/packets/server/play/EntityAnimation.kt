package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftEnum
import br.com.devsrsouza.ktmcpacket.MinecraftEnumType.BYTE
import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import kotlinx.serialization.Serializable

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

// 0x06
@Serializable
data class EntityAnimation(
    @MinecraftNumber(VAR)
    val entityId: Int,

    val animation: Animation // Unsigned Byte
) : ServerPacket