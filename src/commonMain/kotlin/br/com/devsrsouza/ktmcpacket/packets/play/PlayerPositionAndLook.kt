package br.com.devsrsouza.ktmcpacket.packets.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import kotlinx.serialization.Serializable

object PlayerPositionAndLookPacket {
    @Serializable
    data class Server(
            val x: Double,
            val y: Double,
            val z: Double,
            val yaw: Float,
            val pitch: Float,
            val flags: Byte,
            @MinecraftNumber(VAR) val teleportId: Int
    )
}