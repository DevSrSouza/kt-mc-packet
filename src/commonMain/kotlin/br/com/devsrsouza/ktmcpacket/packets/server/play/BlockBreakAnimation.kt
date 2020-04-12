package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.Position
import kotlinx.serialization.Serializable

// 0x09
@Serializable
data class BlockBreakAnimation(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val entityId: Int,
    val location: Position,
    val destroyState: Byte // 0-9
) : ServerPacket