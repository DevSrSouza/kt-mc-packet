package br.com.devsrsouza.ktmcpacket.types

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import kotlinx.serialization.Serializable

interface Location<LOOK_TYPE> {
    val x: Double
    val y: Double
    val z: Double
    val yaw: LOOK_TYPE
    val pitch: LOOK_TYPE
}

@Serializable
data class EntityLocation(
    override val x: Double,
    override val y: Double,
    override val z: Double,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    override val pitch: Byte, // Angle: Unsigned Byte 1-256

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    override val yaw: Byte, // Angle: Unsigned Byte 1-256
) : Location<Byte>

@Serializable
data class PlayerLocation(
    override val x: Double,
    override val y: Double,
    override val z: Double,
    override val yaw: Float,
    override val pitch: Float,
) : Location<Float>