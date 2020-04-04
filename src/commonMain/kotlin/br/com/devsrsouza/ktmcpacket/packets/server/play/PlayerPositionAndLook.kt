package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import kotlinx.serialization.Serializable

@Serializable
data class PlayerPositionAndLook(
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float,
        val flags: Byte,
        @MinecraftNumber(VAR) val teleportId: Int
)