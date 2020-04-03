import br.com.devsrsouza.ktmcpacket.MinecraftProtocol
import br.com.devsrsouza.ktmcpacket.packets.HandshakePacket
import br.com.devsrsouza.ktmcpacket.packets.status.PingPongPacket
import br.com.devsrsouza.ktmcpacket.packets.status.ServerListPingPacket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.readFully
import org.intellij.lang.annotations.Language
import java.io.DataOutputStream
import java.io.OutputStream
import java.net.ServerSocket
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * This is a mini server socket as a Minecraft Server
 * that works only with Status request.
 */
suspend fun main() {
    val serverSocket = ServerSocket(25565)

    println("started the ServerSocket")

    val socket = serverSocket.accept()
    val input = socket.getInputStream().toByteReadChannel()
    val output = DataOutputStream(socket.getOutputStream())

    println("Received a connection.")

    // handshake
    println("handshake start")
    var packetLength = input.readVarInt()
    println("packetLength: $packetLength")

    var packetId = input.readVarInt()
    println("packetId: $packetId")

    // -1 because the packetId was read
    var packetByteArray = ByteArray(packetLength - 1)
    input.readFully(packetByteArray)

    val handshake = MinecraftProtocol.load(
            HandshakePacket.Client.serializer(),
            packetByteArray
    )
    println(handshake)

    // request
    println("request start")
    packetLength = input.readVarInt()
    println("packetLength: $packetLength")

    packetId = input.readVarInt()
    println("packetId: $packetId")

    println("sending response")

    var response = MinecraftProtocol.dump(
            ServerListPingPacket.Server.serializer(),
            ServerListPingPacket.Server(STATUS_RESPONSE)
    )

    output.writeVarInt(response.size+1)
    output.writeByte(0x00)

    output.write(response)

    println("ping start")
    packetLength = input.readVarInt()
    println("packetLength: $packetLength")

    packetId = input.readVarInt()
    println("packetId: $packetId")

    // -1 because the packetId was read
    packetByteArray = ByteArray(packetLength - 1)
    input.readFully(packetByteArray)

    val ping = MinecraftProtocol.load(
        PingPongPacket.Client.serializer(),
        packetByteArray
    )

    println(ping)

    println("sending pong back")

    response = MinecraftProtocol.dump(
        PingPongPacket.Server.serializer(),
        PingPongPacket.Server(ping.payload)
    )

    output.writeVarInt(response.size+1)
    output.writeByte(0x01)

    output.write(response)
}

@Language("JSON")
val STATUS_RESPONSE = """
    {"version":{"name":"1.15.2","protocol":578},"players":{"max":1,"online":500,"sample":[]},"description":{"text":"kt-mc-packet test"}}
""".trimIndent()

suspend fun ByteReadChannel.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++
        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while (read and 128.toByte() != 0.toByte())
    return result
}

fun OutputStream.writeVarInt(value: Int) {
    var value = value
    val output = DataOutputStream(this)
    do {
        var temp = (value and 127).toByte()
        // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
        value = value ushr 7
        if (value != 0) {
            temp = temp or 128.toByte()
        }
        output.writeByte(temp.toInt())
    } while (value != 0)
}