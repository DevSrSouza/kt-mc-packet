package br.com.devsrsouza.ktmcpacket.packets.server.play

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://wiki.vg/Protocol#Chunk_Data
/*@Serializable
data class ChunkData(
    val chunkX: Int,
    val chunkZ: Int,

    val fullChunk: Boolean,

    @MinecraftNumber(VAR)
    val availableSections: Int, // Y
    @NBT
    val heightmaps: Heightmaps, // NBT
    val biomes: IntArray?, // optional
    @NBT
    val blockEntities: Array<NBT>
) : ServerPacket*/

@Serializable
data class Heightmaps(
    @SerialName("MOTION_BLOCKING")
    val motionBlocking: LongArray
)

interface NBT
