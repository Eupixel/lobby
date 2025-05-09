package net.eupixel

import kotlinx.coroutines.runBlocking
import net.eupixel.util.PocketBaseFileClient
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.anvil.AnvilLoader
import net.eupixel.event.EventManager
import java.io.File
import java.util.zip.ZipInputStream
import kotlin.io.path.Path

fun unzip(zipFile: String, outputDir: String) {
    ZipInputStream(File(zipFile).inputStream()).use { zin ->
        val tgt = File(outputDir).absoluteFile
        generateSequence { zin.nextEntry }.forEach { e ->
            val rel = e.name.substringAfter('/', "")
            if (rel.isEmpty()) return@forEach
            val outFile = File(tgt, rel)
            if (e.isDirectory) outFile.mkdirs() else {
                outFile.parentFile!!.mkdirs()
                outFile.outputStream().use { os -> zin.copyTo(os) }
            }
            zin.closeEntry()
        }
    }
}

fun startServer() {
    val server = MinecraftServer.init()
    val instance = MinecraftServer
        .getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby") }
    EventManager(MinecraftServer.getGlobalEventHandler(), instance)
    server.start("0.0.0.0", 25565)
}

fun main() = runBlocking {
    PocketBaseFileClient()
        .downloadFileAwait(System.getenv("LOBBY_COLL"), System.getenv("LOBBY_RECORD"), Path("lobby.zip"))
    unzip("lobby.zip", "lobby")
    startServer()
}