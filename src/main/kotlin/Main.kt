package net.eupixel

import kotlinx.coroutines.runBlocking
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.anvil.AnvilLoader
import net.eupixel.event.EventManager
import net.eupixel.vivlib.util.WebDavClient
import net.minestom.server.extras.MojangAuth
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

fun unzip(zipFile: String, outputDir: String) {
    ZipInputStream(File(zipFile).inputStream()).use { zin ->
        val targetDir = File(outputDir).absoluteFile
        var entry = zin.nextEntry
        while (entry != null) {
            val name = entry.name.substringAfter('/', "")
            if (name.isNotEmpty()) {
                val outFile = File(targetDir, name)
                if (entry.isDirectory) outFile.mkdirs() else {
                    outFile.parentFile!!.mkdirs()
                    FileOutputStream(outFile).use { os -> zin.copyTo(os) }
                }
            }
            zin.closeEntry()
            entry = zin.nextEntry
        }
    }
}

fun startServer() {
    val server = MinecraftServer.init()
    val instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("lobby") }
    EventManager(MinecraftServer.getGlobalEventHandler(), instance)
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}



fun main(): Unit = runBlocking {
    val client = WebDavClient()
    val data = client.awaitFile("lobby.zip")
    FileOutputStream("lobby.zip").use { it.write(data) }
    unzip("lobby.zip", "lobby")
    startServer()
}