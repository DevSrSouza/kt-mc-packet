package br.com.devsrsouza.ktmcpacket.packets.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.*
import kotlinx.serialization.Serializable

object SpawnExperienceOrbPacket {

    @Serializable
    data class Server(
        @MinecraftNumber(VAR)
        val entityId: Int,

        val x: Double,
        val y: Double,
        val z: Double,

        val count: Short // The amount of experience this orb will reward once collected
    )
}