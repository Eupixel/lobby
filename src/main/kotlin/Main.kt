package net.eupixel

import net.eupixel.core.DirectusClient
import kotlinx.coroutines.runBlocking
import net.eupixel.command.CommandManager
import net.eupixel.core.DBTranslator
import net.eupixel.core.Messenger
import net.eupixel.event.EventManager
import net.eupixel.save.SaveManager
import net.eupixel.save.saves.Config
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {
    DirectusClient.initFromEnv()
    Config.translator = DBTranslator(arrayOf("chat", "whereami", "flight_true", "flight_false", "prefix"))
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

    Config.instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby"); timeRate = 0 }

    SaveManager.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}