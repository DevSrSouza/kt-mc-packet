object Versions {
    val ktxSerialization = "1.1.0"
    val kotlinPoet = "1.7.2"
    val ktor = "1.5.2"
    val benasherUuid = "0.2.3"
}

object Dependencies {
    val ktxSerializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.ktxSerialization}"
    val ktxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.ktxSerialization}"
    val ktxSerializationProtoBuf = "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${Versions.ktxSerialization}"

    val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"

    val ktorIO = "io.ktor:ktor-io:${Versions.ktor}"
    val ktorNetwork = "io.ktor:ktor-network:${Versions.ktor}"

    val benasher44Uuid = "com.benasher44:uuid:${Versions.benasherUuid}"
}