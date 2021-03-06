package br.com.devsrsouza.ktmcpacket.types

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

@Serializer(forClass = Uuid::class)
object UUIDSerializer : KSerializer<Uuid> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.mostSignificantBits)
            encodeLongElement(descriptor, 1, value.leastSignificantBits)
        }
    }

    override fun deserialize(decoder: Decoder): Uuid {
        val mostSignificationBits = decoder.decodeLong()
        val leastSignificationBits = decoder.decodeLong()

        return Uuid(mostSignificationBits, leastSignificationBits)
    }
}

@Serializer(forClass = Uuid::class)
object UUIDStringSerializer : KSerializer<Uuid> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uuid {
        val uuidString = decoder.decodeString()

        return uuidFrom(uuidString)
    }
}