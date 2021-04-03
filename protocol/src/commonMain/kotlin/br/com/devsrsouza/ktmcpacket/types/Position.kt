package br.com.devsrsouza.ktmcpacket.types

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

internal fun Double.toPositionInt(): Int = (this * 32.0).toInt()
internal fun Int.toPositionDouble(): Double = this / 32.0

fun Position(
    x: Double,
    y: Double,
    z: Double
) = Position(
    x.toPositionInt(),
    y.toPositionInt(),
    z.toPositionInt()
)

/**
 * @see https://wiki.vg/Protocol#Position
 */

@Serializable
data class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {

    @Serializer(forClass = Position::class)
    companion object : KSerializer<Position> {
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("br.com.devsrsouza.ktmcpacket.types.Position") {
                element<Long>("position")
            }

        override fun deserialize(decoder: Decoder): Position =
            decoder.decodeStructure(descriptor) {
                var positionContent: Long? = null

                if (decodeSequentially()) { // sequential decoding protocol
                    positionContent = decodeLongElement(descriptor, 0)
                } else while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> positionContent = decodeLongElement(descriptor, 0)
                        CompositeDecoder.DECODE_DONE -> break
                        else -> error("Unexpected index: $index")
                    }
                }

                requireNotNull(positionContent)

                longToPosition(positionContent)
            }


        override fun serialize(encoder: Encoder, value: Position) {
            encoder.encodeStructure(descriptor) {
                encodeLongElement(descriptor, 0, positionToLong(value))
            }
        }

        private fun positionToLong(position: Position): Long = with(position) {
            x.toLong() and 0x3FFFFFF shl 38 or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF)
        }

        private fun longToPosition(long: Long): Position = Position(
            (long shr 38).toInt(),
            (long and 0xFFF).toInt(),
            (long shl 26 shr 38).toInt()
        )

    }
}