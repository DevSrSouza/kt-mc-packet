package br.com.devsrsouza.ktmcpacket.types

import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val x: Double,
    val y: Double,
    val z: Double,

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val pitch: Byte, // Angle: Unsigned Byte 1-256

    @MinecraftNumber(MinecraftNumberType.UNSIGNED)
    val yaw: Byte, // Angle: Unsigned Byte 1-256
)