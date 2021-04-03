import br.com.devsrsouza.ktmcpacket.*
import br.com.devsrsouza.ktmcpacket.PacketContent.*
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.packets.client.play.ClientKeepAlive
import br.com.devsrsouza.ktmcpacket.packets.client.play.LoginStart
import br.com.devsrsouza.ktmcpacket.packets.server.login.LoginSuccess
import br.com.devsrsouza.ktmcpacket.packets.server.play.*
import br.com.devsrsouza.ktmcpacket.types.EntityLocation
import br.com.devsrsouza.ktmcpacket.types.PlayerLocation
import br.com.devsrsouza.ktmcpacket.utils.minecraft
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * This is a mini server socket as a Minecraft Server
 * that works only with Status request.
 */
@OptIn(ExperimentalTime::class)
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
    val handshake = input.minecraft.readClientPacket(PacketState.HANDSHAKE) as Found<Handshake>
    println(handshake)

    val loginStart = input.minecraft.readClientPacket(PacketState.LOGIN) as Found<LoginStart>
    println(loginStart)

    println("sending Login success")

    output.minecraft.writePacket(PacketState.LOGIN, LoginSuccess(UUID.randomUUID(), loginStart.packet.nickname))

    println("seding join game")
    output.minecraft.writePacket(
        PacketState.PLAY,
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

    println("sending player position")
    output.minecraft.writePacket(
        PacketState.PLAY,
        PlayerPositionAndLook(
            PlayerLocation(
                0.0,
                64.0,
                0.0,
                130f,
                25f
            ),
            0x00,
            1
        )
    )

    println("login complete!")

    // others packets

    delay(1000)

    println("sending spawn living entity")
    output.minecraft.writePacket(
        PacketState.PLAY,
        SpawnLivingEntity(
            3,
            UUID.randomUUID(),
            14,
            EntityLocation(
                -1.5,
                64.0,
                -1.5,
                0u.toByte(),
                0u.toByte(),
            ),
            0u.toByte(),

            0,
            0,
            0
        )
    )

    val clientKeepAlivePacketType = PacketState.PLAY.byKClass[ClientKeepAlive::class]
    requireNotNull(clientKeepAlivePacketType)

    // send keep alive
    while(true) {
        delay(20.seconds)

        println("Sending Keep Alive")
        val keepAliveId = System.currentTimeMillis()

        val timeTaken = measureTimeMillis {
            output.minecraft.writePacket(
                PacketState.PLAY,
                ServerKeepAlive(keepAliveId)
            )

            var keepAlive: PacketContent.Found<ClientKeepAlive>? = null
            while(keepAlive == null) {
                println("reading new packet looking for KeepAlive")
                val packet = input.minecraft.readClientPacket(PacketState.PLAY)
                when(packet) {
                    is Found -> {
                        if(packet.type.id == clientKeepAlivePacketType.id) {
                            keepAlive = packet as PacketContent.Found<ClientKeepAlive>
                        } else {
                            println("Received packet that is register but is not keepAlive\n$packet")
                        }
                    }
                    is NotFound -> {
                        val formattedId = String.format("0x%02X ", packet.id.toByte())
                        println("Received packet with id=$formattedId that is not register by the library\n$packet")
                    }
                }
            }
            println("Received a keepAlive packet from client: $keepAlive")
        }

        println("It took ${timeTaken}ms to the KeepAlive response.")
    }
}
