package br.com.devsrsouza.ktmcpacket.packets.play

import br.com.devsrsouza.ktmcpacket.*
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType.VAR
import kotlinx.serialization.*

object JoinGamePacket {

    @Serializable
    data class Server(
        val entityId: Int,
        val gameMode: GameMode,
        val dimension: Dimension,
        val hashedSeed: Long,
        val maxPlayers: Byte,
        val levelType: LevelType,
        @MinecraftNumber(VAR) val viewDistance: Int,
        val reducedDebugInfo: Boolean,
        val enableRespawnScreen: Boolean
    )

    @Serializable
    @MinecraftEnum(MinecraftEnumType.UNSIGNED_BYTE)
    enum class GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SPECTATOR
    }

    @Serializable
    @MinecraftEnum(MinecraftEnumType.INT)
    enum class Dimension {
        @SerialOrdinal(-1) NETHER,
        @SerialOrdinal(0) OVERWORLD,
        @SerialOrdinal(1) END
    }

    @Serializable
    @MinecraftEnum(MinecraftEnumType.STRING)
    @MinecraftString(16)
    enum class LevelType {
        @SerialName("default") DEFAULT,
        @SerialName("flat") FLAT,
        @SerialName("largeBiomes") LARGE_BIOMES,
        @SerialName("amplified") AMPLIFIED,
        @SerialName("customized") CUSTOMIZED,
        @SerialName("buffet") BUFFET,
        @SerialName("default_1_1") DEFAULT_1_1;
    }
}