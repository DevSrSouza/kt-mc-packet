package br.com.devsrsouza.ktmcpacket.packets.server.play

import br.com.devsrsouza.ktmcpacket.MinecraftEnum
import br.com.devsrsouza.ktmcpacket.MinecraftEnumType.BYTE
import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.SerialOrdinal
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import kotlinx.serialization.Serializable

@Serializable
@MinecraftEnum(BYTE)
enum class WeatherEntityType {
    @SerialOrdinal(1) THUNDERBOLT
}

@Serializable
data class SpawnWeatherEntity(
        @MinecraftNumber(MinecraftNumberType.VAR)
        val entityId: Int,

        val type: WeatherEntityType,

        val x: Double,
        val y: Double,
        val z: Double
) : ServerPacket