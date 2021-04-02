package br.com.devsrsouza.ktmcpacket.serialization

import br.com.devsrsouza.ktmcpacket.MinecraftEnumType
import br.com.devsrsouza.ktmcpacket.MinecraftNumberType

data class ProtocolDesc(
    val type: MinecraftNumberType,
    val stringMaxLength: Int
)

data class ProtocolEnumDesc(
    val type: MinecraftEnumType,
    val stringMaxLength: Int
)

data class ProtocolEnumElementDesc(
    val ordinal: Int
)
