package br.com.devsrsouza.ktmcpacket

import br.com.devsrsouza.ktmcpacket.packets.ClientPacket
import br.com.devsrsouza.ktmcpacket.packets.Packet
import br.com.devsrsouza.ktmcpacket.packets.ServerPacket
import br.com.devsrsouza.ktmcpacket.packets.client.Handshake
import br.com.devsrsouza.ktmcpacket.packets.client.play.LoginStart
import br.com.devsrsouza.ktmcpacket.packets.client.status.Ping
import br.com.devsrsouza.ktmcpacket.packets.client.status.Request
import br.com.devsrsouza.ktmcpacket.packets.server.login.LoginSuccess
import br.com.devsrsouza.ktmcpacket.packets.server.play.*
import br.com.devsrsouza.ktmcpacket.packets.server.status.Pong
import br.com.devsrsouza.ktmcpacket.packets.server.status.ServerListPing
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
import br.com.devsrsouza.ktmcpacket.packets.client.play.ClientKeepAlive
import br.com.devsrsouza.ktmcpacket.packets.server.play.ServerKeepAlive

typealias PacketId = Int

enum class PacketState(
    val client: List<PacketType<out ClientPacket>>,
    val server: List<PacketType<out ServerPacket>>
) {
    HANDSHAKE(
        client = listOf(
            PacketType(0x00, Handshake::class, Handshake.serializer())
        ),
        server = emptyList()
    ),
    STATUS(
        client = listOf(
            PacketType(0x00, Request::class, Request.serializer()),
            PacketType(0x01, Ping::class, Ping.serializer())
        ),
        server = listOf(
            PacketType(0x00, ServerListPing::class, ServerListPing.serializer()),
            PacketType(0x01, Pong::class, Pong.serializer())
        )
    ),
    LOGIN(
        client = listOf(
            PacketType(0x00, LoginStart::class, LoginStart.serializer())
        ),
        server = listOf(
            PacketType(0x02, LoginSuccess::class, LoginSuccess.serializer())
        )
    ),
    PLAY(
        client = listOf(
            PacketType(0x0F, ClientKeepAlive::class, ClientKeepAlive.serializer()),
        ),
        server = listOf(
            PacketType(0x00, SpawnEntity::class, SpawnEntity.serializer()),
            PacketType(0x01, SpawnExperienceOrb::class, SpawnExperienceOrb.serializer()),
            PacketType(0x02, SpawnWeatherEntity::class, SpawnWeatherEntity.serializer()),
            PacketType(0x03, SpawnLivingEntity::class, SpawnLivingEntity.serializer()),
            PacketType(0x05, SpawnPlayer::class, SpawnPlayer.serializer()),
            PacketType(0x06, EntityAnimation::class, EntityAnimation.serializer()),
            PacketType(0x08, AcknowledgePlayerDigging::class, AcknowledgePlayerDigging.serializer()),
            PacketType(0x09, BlockBreakAnimation::class, BlockBreakAnimation.serializer()),
            PacketType(0x21, ServerKeepAlive::class, ServerKeepAlive.serializer()),
            PacketType(0x26, JoinGame::class, JoinGame.serializer()),
            PacketType(0x36, PlayerPositionAndLook::class, PlayerPositionAndLook.serializer()),
        )
    );

    val byKClass: Map<KClass<out Packet>, PacketType<out Packet>>
            = client.associateBy { it.kclass } + server.associateBy { it.kclass }

    val clientById: Map<PacketId, PacketType<out ClientPacket>> = client.associateBy { it.id }
    val serverById: Map<PacketId, PacketType<out ServerPacket>> = server.associateBy { it.id }
}

data class PacketType<T : Packet>(
    val id: PacketId,
    val kclass: KClass<T>,
    val serializer: KSerializer<T>
)