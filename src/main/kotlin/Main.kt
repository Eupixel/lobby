package net.eupixel

import core.Vivlib
import net.eupixel.vivlib.core.DirectusClient
import kotlinx.coroutines.runBlocking
import net.eupixel.command.CommandManager
import net.eupixel.vivlib.core.DBTranslator
import net.eupixel.core.MessageHandler
import net.eupixel.vivlib.core.WhitelistManager
import net.eupixel.event.EventManager
import net.eupixel.save.Config
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {
    MessageHandler.start()
    WhitelistManager.start()
    DirectusClient.initFromEnv()
    DBTranslator.loadFromDB()
    Vivlib.init()

    runBlocking {
        if (DirectusClient.downloadFile("worlds", "name", "lobby", "world_data", "lobby.zip")) {
            Helper.unzip("lobby.zip", "lobby")
        }
    }

    val server = MinecraftServer.init()
    Config.instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby"); timeRate = 0 }

    Config.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}