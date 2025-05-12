package net.eupixel

import kotlinx.coroutines.runBlocking
import net.eupixel.event.EventManager
import net.eupixel.vivlib.util.DirectusClient
import net.eupixel.vivlib.util.Helper
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {
    val host = System.getenv("HOST") ?: "none"
    val token = System.getenv("TOKEN") ?: "none"

    if (host != "none" && token != "none") {
        DirectusClient.init(host, token)
        runBlocking {
            val result = DirectusClient.downloadWorld("lobby")
            if (result.first) {
                println("Spawn-Position: ${result.second}")
                Helper.unzip("lobby.zip", "lobby")
            }
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