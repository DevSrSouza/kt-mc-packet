import br.com.devsrsouza.ktmcpacket.MinecraftProtocol
import br.com.devsrsouza.ktmcpacket.packets.HandshakePacket
import br.com.devsrsouza.ktmcpacket.packets.login.LoginStartPacket
import br.com.devsrsouza.ktmcpacket.packets.login.LoginSuccessPacket
import br.com.devsrsouza.ktmcpacket.packets.play.JoinGamePacket
import br.com.devsrsouza.ktmcpacket.packets.play.PlayerPositionAndLookPacket
import br.com.devsrsouza.ktmcpacket.packets.status.PingPongPacket
import br.com.devsrsouza.ktmcpacket.packets.status.ServerListPingPacket
import com.benasher44.uuid.Uuid
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.Output
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.readFully
import kotlinx.coroutines.delay
import kotlinx.serialization.InternalSerializationApi
import org.intellij.lang.annotations.Language
import java.io.DataOutputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.util.*
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
    println("login start")
    packetLength = input.readVarInt()
    println("packetLength: $packetLength")

    packetId = input.readVarInt()
    println("packetId: $packetId")

    packetByteArray = ByteArray(packetLength - 1)
    input.readFully(packetByteArray)

    val loginStart = MinecraftProtocol.load(
            LoginStartPacket.Client.serializer(),
            packetByteArray
    )

    println(loginStart)

    println("sending Login success")

    var response = MinecraftProtocol.dump(
            LoginSuccessPacket.Server.serializer(),
            LoginSuccessPacket.Server(UUID.randomUUID(), loginStart.nickname)
    )

    var packet = BytePacketBuilder().apply {
        writeVarInt(0x02)
        writeFully(response)
    }

    output.writeVarInt(packet.size)
    output.write(packet.build().readBytes())

    println("seding join game")
    response = MinecraftProtocol.dump(
            JoinGamePacket.Server.serializer(),
            JoinGamePacket.Server(
                    1,
                    JoinGamePacket.GameMode.ADVENTURE,
                    JoinGamePacket.Dimension.OVERWORLD,
                    0.toLong(),
                    10,
                    JoinGamePacket.LevelType.DEFAULT,
                    32,
                    false,
                    false
            )
    )

    packet = BytePacketBuilder().apply {
        writeVarInt(0x26)
        writeFully(response)
    }
    output.writeVarInt(packet.size)
    output.write(packet.build().readBytes())

    println("seding player position")
    response = MinecraftProtocol.dump(
            PlayerPositionAndLookPacket.Server.serializer(),
            PlayerPositionAndLookPacket.Server(
                    0.0,
                    64.0,
                    0.0,
                    180f,
                    180f,
                    0x01,
                    1
            )
    )

    packet = BytePacketBuilder().apply {
        writeByte(0x36)
        writeFully(response)
    }
    output.writeVarInt(packet.size)
    output.write(packet.build().readBytes())

    delay(60000)
}

fun Output.writeVarInt(value: Int) {
    var value = value
    do {
        var temp = (value and 127).toByte()
        // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
        value = value ushr 7
        if (value != 0) {
            temp = temp or 128.toByte()
        }
        writeByte(temp)
    } while (value != 0)
}