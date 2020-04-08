package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.types.UUIDSerializer
import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class SpawnPlayer(
    @MinecraftNumber(MinecraftNumberType.VAR)
    val entityId: Int,

    @Serializable(with = UUIDSerializer::class)
    val uuid: Uuid,

    val x: Double,
    val y: Double,
    val z: Double,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val yaw: Byte, // Angle: Unsigned Byte 1-256

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val pitch: Byte // Angle: Unsigned Byte 1-256
)