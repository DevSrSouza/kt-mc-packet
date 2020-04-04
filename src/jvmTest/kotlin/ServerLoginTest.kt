import br.com.devsrsouza.ktmcpacket.MinecraftProtocol
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.packets.client.play.LoginStart
import br.com.devsrsouza.ktmcpacket.packets.server.login.LoginSuccess
import br.com.devsrsouza.ktmcpacket.packets.server.play.*
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.Output
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.readFully
import kotlinx.coroutines.delay
import java.io.DataOutputStream
import java.net.ServerSocket
import java.util.*
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
            Handshake.serializer(),
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
            LoginStart.serializer(),
            packetByteArray
    )

    println(loginStart)

    println("sending Login success")

    var response = MinecraftProtocol.dump(
            LoginSuccess.serializer(),
            LoginSuccess(UUID.randomUUID(), loginStart.nickname)
    )

    var packet = BytePacketBuilder().apply {
        writeVarInt(0x02)
        writeFully(response)
    }

    output.writeVarInt(packet.size)
    output.write(packet.build().readBytes())

    println("seding join game")
    response = MinecraftProtocol.dump(
            JoinGame.serializer(),
            JoinGame(
                    1,
                    GameMode.ADVENTURE,
                    Dimension.OVERWORLD,
                    0.toLong(),
                    10,
                    LevelType.DEFAULT,
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

    println("sending player position")
    response = MinecraftProtocol.dump(
            PlayerPositionAndLook.serializer(),
            PlayerPositionAndLook(
                    0.0,
                    64.0,
                    0.0,
                    130f,
                    25f,
                    0x00,
                    1
            )
    )

    packet = BytePacketBuilder().apply {
        writeByte(0x36)
        writeFully(response)
    }
    output.writeVarInt(packet.size)
    output.write(packet.build().readBytes())

    println("login complete!")

    // others packets

    delay(1000)

    println("sending spawn living entity")
    response = MinecraftProtocol.dump(
        SpawnLivingEntity.serializer(),
        SpawnLivingEntity(
            3,
            UUID.randomUUID(),
            14,
            -1.5,
            64.0,
            -1.5,
            0u.toByte(),
            0u.toByte(),
            0u.toByte(),
            0,
            0,
            0
        )
    )

    packet = BytePacketBuilder().apply {
        writeByte(0x03)
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