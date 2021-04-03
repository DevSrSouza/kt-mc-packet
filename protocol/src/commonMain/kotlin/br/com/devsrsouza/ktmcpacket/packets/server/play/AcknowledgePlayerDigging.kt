package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.Position
import kotlinx.serialization.Serializable

// 0x08
@Serializable
data class AcknowledgePlayerDigging(
    val location: Position,

    @MinecraftNumber(MinecraftNumberType.VAR)
    val block: Int,

    val status: DiggingStatus,

    val successful: Boolean
) : ServerPacket

enum class DiggingStatus {
    STARTED,
    CANCELLED,
    FINISHED
}