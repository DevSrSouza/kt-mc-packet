package br.com.devsrsouza.ktmcpacket.types

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializer(forClass = Uuid::class)
object UuidSerializer : KSerializer<Uuid> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.benasher44.uuid.Uuid:Binary") {
            element<Long>("msb")
            element<Long>("lsb")
        }

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.mostSignificantBits)
            encodeLongElement(descriptor, 1, value.leastSignificantBits)
        }
    }

    override fun deserialize(decoder: Decoder): Uuid =
        decoder.decodeStructure(descriptor) {
            var mostSignificationBits: Long? = null
            var leastSignificationBits: Long? = null

            if (decodeSequentially()) { // sequential decoding protocol
                mostSignificationBits = decodeLongElement(descriptor, 0)
                leastSignificationBits = decodeLongElement(descriptor, 1)
            } else while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> mostSignificationBits = decodeLongElement(descriptor, 0)
                    1 -> leastSignificationBits = decodeLongElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            requireNotNull(mostSignificationBits)
            requireNotNull(leastSignificationBits)

            Uuid(mostSignificationBits, leastSignificationBits)
        }
}

@Serializer(forClass = Uuid::class)
object UuidStringSerializer : KSerializer<Uuid> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.benasher44.uuid.Uuid:String") {
            element<String>("uuid")
        }

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.toString())
        }
    }

    override fun deserialize(decoder: Decoder): Uuid =
        decoder.decodeStructure(descriptor) {
            var uuid: String? = null

            if (decodeSequentially()) { // sequential decoding protocol
                uuid = decodeStringElement(UuidSerializer.descriptor, 0)
            } else while (true) {
                when (val index = decodeElementIndex(UuidSerializer.descriptor)) {
                    0 -> uuid = decodeStringElement(UuidSerializer.descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            requireNotNull(uuid)

            uuidFrom(uuid)
        }
}