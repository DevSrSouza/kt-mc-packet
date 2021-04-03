package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.*
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import kotlinx.serialization.Serializable

@Serializable
data class SpawnExperienceOrb(
        @MinecraftNumber(VAR)
        val entityId: Int,

        val x: Double,
        val y: Double,
        val z: Double,

        val count: Short // The amount of experience this orb will reward once collected
) : ServerPacket