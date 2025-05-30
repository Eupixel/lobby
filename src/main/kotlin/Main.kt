package net.eupixel

import net.eupixel.core.DirectusClient
import kotlinx.coroutines.runBlocking
import net.eupixel.command.CommandManager
import net.eupixel.core.DBTranslator
import net.eupixel.core.Messenger
import net.eupixel.event.EventManager
import net.eupixel.save.Config
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.network.packet.server.common.TransferPacket

fun main() {
    DirectusClient.initFromEnv()
    Config.translator = DBTranslator(arrayOf("whereami", "flight_true", "flight_false", "prefix", "invalid_gamemode", "queue_usage", "queue_left", "queue_joined", "queue_already", "queue_none"))
    val server = MinecraftServer.init()

    runBlocking {
        val ok = DirectusClient.downloadFile("worlds", "name", "lobby", "world_data", "lobby.zip")
        if (ok) {
            Helper.unzip("lobby.zip", "lobby")
        } else {
            println("Failed to download the lobby.")
        }
    }

    Messenger.bind("0.0.0.0", 2905)
    Messenger.registerTarget("entrypoint", "entrypoint", 2905)
    Messenger.addListener("transfer") { msg ->
        val name = msg.split("?")[0]
        val target = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendPacket(TransferPacket(target.split("&")[0], target.split("&")[1].toInt()))
            }
        }
    }

    Config.instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby"); timeRate = 0 }

    Config.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}