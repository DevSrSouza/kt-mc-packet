package br.com.devsrsouza.ktmcpacket

import kotlinx.serialization.SerialInfo

enum class MinecraftNumberType {
    DEFAULT, UNSIGNED, VAR
}

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class MinecraftNumber(
        val type: MinecraftNumberType = MinecraftNumberType.DEFAULT
)

enum class MinecraftEnumType {
    VARINT, BYTE
}

@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class MinecraftEnum(
    val type: MinecraftEnumType = MinecraftEnumType.VARINT
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class SerialOrdinal(
        val ordinal: Int
)