package net.eupixel

import kotlinx.coroutines.runBlocking
import net.eupixel.event.EventManager
import net.eupixel.vivlib.util.DirectusClient
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {
    DirectusClient.initFromEnv()

    runBlocking {
        val (ok, spawnPos) = DirectusClient.downloadWorld("lobby")
        if (ok) {
            println("spawn_position=$spawnPos")
            Helper.unzip("lobby.zip", "lobby")
        } else {
            println("Failed to download the lobby.")
        }
    }

    val server = MinecraftServer.init()
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    val instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby") }

    EventManager(globalEventHandler, instance)
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}