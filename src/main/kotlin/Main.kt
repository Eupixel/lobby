package net.eupixel

import kotlinx.coroutines.runBlocking
import net.eupixel.event.EventManager
import net.eupixel.util.Config
import net.eupixel.util.DecorationLoader
import net.eupixel.vivlib.util.DirectusClient
import net.eupixel.vivlib.util.DirectusClient.getData
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {
    DirectusClient.initFromEnv()

    runBlocking {
        val minY = getData("lobby_values", "name", "min_y", listOf("data"))
            ?.get("data")
            ?.asInt()
        if (minY != null) {
            Config.minY = minY
        }
        val ok = DirectusClient.downloadWorld("lobby")
        if (ok) {
            Helper.unzip("lobby.zip", "lobby")
        } else {
            println("Failed to download the lobby.")
        }
    }

    val server = MinecraftServer.init()
    Config.instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby") }

    DecorationLoader.init()
    EventManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}