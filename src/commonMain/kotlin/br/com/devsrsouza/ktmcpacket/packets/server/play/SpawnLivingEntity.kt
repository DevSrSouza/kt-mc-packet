package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.Location
import br.com.devsrsouza.ktmcpacket.types.UuidSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class SpawnLivingEntity(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val entityId: Int,

    @Serializable(with = UuidSerializer::class)
    val uuid: Uuid,

    @MinecraftNumber(MinecraftNumberType.VAR)
    val type: Int, // FIXME: Type as Enum, same as SpawnEntity

    val location: Location,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val headPitch: Byte, // Angle: Unsigned Byte 1-256

    val velocityX: Short,
    val velocityY: Short,
    val velocityZ: Short
) : ServerPacket