package net.eupixel

import kotlinx.coroutines.runBlocking
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.anvil.AnvilLoader
import net.eupixel.event.EventManager
import net.eupixel.vivlib.util.Helper
import net.eupixel.vivlib.util.WebDavClient
import net.minestom.server.extras.MojangAuth
import java.io.FileOutputStream

fun startServer() {
    val server = MinecraftServer.init()
    val instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby") }
    EventManager(MinecraftServer.getGlobalEventHandler(), instance)
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}

fun main() {
    runBlocking {
        val data = WebDavClient.awaitBytes("lobby.zip")
        FileOutputStream("lobby.zip").use { it.write(data) }
        Helper.unzip("lobby.zip", "lobby")
    }
    startServer()
}