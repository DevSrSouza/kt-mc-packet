package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.EntityLocation
import br.com.devsrsouza.ktmcpacket.types.UuidSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

// 0x05
@Serializable
data class SpawnPlayer(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val entityId: Int,

    @Serializable(with = UuidSerializer::class)
    val uuid: Uuid,

    val location: EntityLocation
) : ServerPacket