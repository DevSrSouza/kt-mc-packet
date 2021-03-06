package br.com.devsrsouza.ktmcpacket.internal

import  br.com.devsrsouza.ktmcpacket.*
import br.com.devsrsouza.ktmcpacket.ProtocolDesc
import br.com.devsrsouza.ktmcpacket.ProtocolEnumDesc
import br.com.devsrsouza.ktmcpacket.ProtocolEnumElementDesc
import kotlinx.serialization.descriptors.SerialDescriptor

internal fun extractParameters(
    descriptor: SerialDescriptor,
    index: Int
): ProtocolDesc {
    val format = descriptor.findElementAnnotation<MinecraftNumber>(index)?.type
        ?: MinecraftNumberType.DEFAULT
    val stringMaxLength = descriptor.findElementAnnotation<MinecraftString>(index)?.maxLength
        ?: MINECRAFT_MAX_STRING_LENGTH
    return ProtocolDesc(format, stringMaxLength)
}

internal fun extractEnumParameters(
    descriptor: SerialDescriptor
): ProtocolEnumDesc {
    val format = descriptor.findEntityAnnotation<MinecraftEnum>()?.type
        ?: MinecraftEnumType.VARINT
    val stringMaxLength = descriptor.findEntityAnnotation<MinecraftString>()?.maxLength
            ?: MINECRAFT_MAX_STRING_LENGTH
    return ProtocolEnumDesc(format, stringMaxLength)
}

internal fun extractEnumElementParameters(
        descriptor: SerialDescriptor,
        index: Int
): ProtocolEnumElementDesc {
    val ordinal = descriptor.findElementAnnotation<SerialOrdinal>(index)?.ordinal
            ?: index

    return ProtocolEnumElementDesc(ordinal)
}

internal fun findEnumIndexByTag(
        descriptor: SerialDescriptor,
        serialOrdinal: Int
): Int = (0 until descriptor.elementsCount).firstOrNull {
    extractEnumElementParameters(
        descriptor,
        it
    ).ordinal == serialOrdinal
} ?: -1

internal inline fun <reified A: Annotation> SerialDescriptor.findElementAnnotation(
    elementIndex: Int
): A? {
    return getElementAnnotations(elementIndex).find { it is A } as A?
}

internal inline fun <reified A: Annotation> SerialDescriptor.findEntityAnnotation(): A? {
    return annotations.find { it is A } as A?
}
