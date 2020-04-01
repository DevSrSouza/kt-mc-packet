# kt-mc-packet [WIP]

A Minecraft packet library for Kotlin multiplatform that uses Kotlinx.serialization 
to Encode and Decode Minecraft packets into objects.

## Usage

Serialize:
```kotlin
val minecraftVersion = 578 // 1.15.2

val byteArray: ByteArray = MinecraftProtocol.dump(
    HandshakePacket.Client.serializer(),
    HandshakePacket.Client(
        minecraftVersion,
        "minecraftserver.com",
        25565,
        HandshakePacket.HandshakeNextState.STATUS
    )
)
```

Deserialize:
```kotlin
val handshake: HandshakePacket.Client = MinecraftProtocol.load(
    HandshakePacket.Client.serializer(),
    byteArrayWithPacketData
)
```