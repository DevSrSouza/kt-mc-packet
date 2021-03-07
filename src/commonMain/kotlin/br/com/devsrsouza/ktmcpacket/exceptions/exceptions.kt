package br.com.devsrsouza.ktmcpacket.exceptions

class PacketNotFoundAtStateException() : Exception()

class MinecraftProtocolEncodingException(message: String) : RuntimeException(message)
class MinecraftProtocolDecodingException(message: String) : RuntimeException(message)