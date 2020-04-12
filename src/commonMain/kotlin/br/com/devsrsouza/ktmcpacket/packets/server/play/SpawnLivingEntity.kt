package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.types.UUIDSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class SpawnLivingEntity(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val entityId: Int,

    @Serializable(with = UUIDSerializer::class)
    val uuid: Uuid,

    @MinecraftNumber(MinecraftNumberType.VAR)
    val type: Int, // FIXME: Type as Enum, same as SpawnEntity

    val x: Double,
    val y: Double,
    val z: Double,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val yaw: Byte, // Angle: Unsigned Byte 1-256

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val pitch: Byte, // Angle: Unsigned Byte 1-256

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val headPitch: Byte, // Angle: Unsigned Byte 1-256

    val velocityX: Short,
    val velocityY: Short,
    val velocityZ: Short
) : ServerPacket