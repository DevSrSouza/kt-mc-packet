package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.PlayerLocation
import kotlinx.serialization.Serializable

// 0x36
@Serializable
data class PlayerPositionAndLook(
        val location: PlayerLocation,
        val flags: Byte,
        @MinecraftNumber(VAR) val teleportId: Int
) : ServerPacket