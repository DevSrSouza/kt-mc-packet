package br.com.devsrsouza.ktmcpacket.packets.play

import br.com.devsrsouza.ktmcpacket.MinecraftEnum
import br.com.devsrsouza.ktmcpacket.MinecraftEnumType.BYTE
import br.com.devsrsouza.ktmcpacket.MinecraftNumber
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType
import br.com.devsrsouza.ktmcpacket.SerialOrdinal
import kotlinx.serialization.Serializable

object SpawnWeatherEntityPacket {

    @Serializable
    @MinecraftEnum(BYTE)
    enum class WeatherEntityType {
        @SerialOrdinal(1) THUNDERBOLT
    }

    @Serializable
    data class Server(
        @MinecraftNumber(MinecraftNumberType.VAR)
        val entityId: Int,

        val type: WeatherEntityType,

        val x: Double,
        val y: Double,
        val z: Double
    )
}