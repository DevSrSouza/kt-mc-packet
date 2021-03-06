package br.com.devsrsouza.ktmcpacket.types

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal fun Double.toPositionInt(): Int = (this * 32.0).toInt()
internal fun Int.toPositionDouble(): Double = this / 32.0

/**
 * @see https://wiki.vg/Protocol#Position
 */

data class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {
    constructor(
        x: Double,
        y: Double,
        z: Double
    ) : this(
        x.toPositionInt(),
        y.toPositionInt(),
        z.toPositionInt()
    )

    @Serializer(forClass = Position::class)
    companion object : KSerializer<Position> {
        override val descriptor: SerialDescriptor
                = PrimitiveSerialDescriptor("Position", PrimitiveKind.LONG)

        override fun deserialize(decoder: Decoder): Position {
            val serialized = decoder.decodeLong()

            return Position(
                (serialized shr 38).toInt(),
                (serialized and 0xFFF).toInt(),
                (serialized shl 26 shr 38).toInt()
            )
        }

        override fun serialize(encoder: Encoder, value: Position) {
            val serialized: Long = with(value) {
                x.toLong() and 0x3FFFFFF shl 38 or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF)
            }

            encoder.encodeLong(serialized)
        }

    }
}