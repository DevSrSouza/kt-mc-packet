import br.com.devsrsouza.ktmcpacket.*
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.utils.minecraft
import br.com.devsrsouza.ktmcpacket.packets.client.status.Ping
import br.com.devsrsouza.ktmcpacket.packets.client.status.Request
import br.com.devsrsouza.ktmcpacket.packets.server.status.Pong
import br.com.devsrsouza.ktmcpacket.packets.server.status.ServerListPing
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.InternalSerializationApi
import org.intellij.lang.annotations.Language

/**
 * This is a mini server socket as a Minecraft Server
 * that works only with Status request.
 */
@InternalSerializationApi
suspend fun main() {
    val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO))
        .tcp()
        .bind("127.0.0.1", 25565)

    println("started the ServerSocket")

    val connection = serverSocket.accept()
    val input = connection.openReadChannel()
    val output = connection.openWriteChannel(autoFlush = false)

    println("Received a connection.")

    // handshake
    println("handshake start")
    val handshake = input.minecraft.readClientPacket(PacketState.HANDSHAKE) as PacketContent.Found<Handshake>
    println(handshake)

    // request
    val request = input.minecraft.readClientPacket(PacketState.STATUS) as PacketContent.Found<Request>
    println(request)

    println("sending response (server ping)")

    output.minecraft.writePacket(PacketState.STATUS, ServerListPing(STATUS_RESPONSE))

    println("ping start")
    val ping = input.minecraft.readClientPacket(PacketState.STATUS) as PacketContent.Found<Ping>
    println(ping)

    println("sending pong back")

    output.minecraft.writePacket(PacketState.STATUS, Pong(ping.packet.payload))
}

@Language("JSON")
val STATUS_RESPONSE = """
    {"version":{"name":"1.15.2","protocol":578},"players":{"max":1,"online":500,"sample":[]},"description":{"text":"kt-mc-packet test"}}
""".trimIndent()
