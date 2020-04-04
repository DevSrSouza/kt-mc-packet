import br.com.devsrsouza.ktmcpacket.MinecraftProtocol
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.packets.client.HandshakeNextState

fun main() {
    val minecraftVersion = 578 // 1.15.2

    val packet = Handshake(
        minecraftVersion,
        "minecraftserver.com",
        25565,
        HandshakeNextState.STATUS
    )

    println(packet)

    val byteArray = MinecraftProtocol.dump(
        Handshake.serializer(),
        packet
    )

    println(byteArray.toHex())

    val handshake = MinecraftProtocol.load(
        Handshake.serializer(),
        byteArray
    )

    println(handshake)
}

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()
fun ByteArray.toHex(): String? {
    val hexChars = CharArray(size * 2)
    for (j in indices) {
        val v: Int = get(j).toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}
