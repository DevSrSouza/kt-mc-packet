package br.com.devsrsouza.ktmcpacket.internal

import br.com.devsrsouza.ktmcpacket.*
import br.com.devsrsouza.ktmcpacket.ProtocolDesc
import br.com.devsrsouza.ktmcpacket.ProtocolEnumDesc
import br.com.devsrsouza.ktmcpacket.ProtocolEnumElementDesc
import kotlinx.serialization.SerialDescriptor

internal fun extractParameters(
    descriptor: SerialDescriptor,
    index: Int
): ProtocolDesc {
    val format = descriptor.findElementAnnotation<MinecraftNumber>(index)?.type
        ?: MinecraftNumberType.DEFAULT
    return ProtocolDesc(format)
}

internal fun extractEnumParameters(
    descriptor: SerialDescriptor
): ProtocolEnumDesc {
    val format = descriptor.findEntityAnnotation<MinecraftEnum>()?.type
        ?: MinecraftEnumType.VARINT
    return ProtocolEnumDesc(format)
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