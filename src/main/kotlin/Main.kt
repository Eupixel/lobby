package net.eupixel

import kotlinx.coroutines.runBlocking
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.anvil.AnvilLoader
import net.eupixel.event.EventManager
import net.eupixel.vivlib.util.WebDavClient
import net.minestom.server.extras.MojangAuth
import java.io.File
import java.io.FileOutputStream
import java.net.Authenticator
import java.net.HttpURLConnection
import java.net.PasswordAuthentication
import java.net.ProtocolException
import java.net.URL
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

@Suppress("DEPRECATION")
fun downloadFallback(key: String): ByteArray {
    val host = System.getenv("WEBDAV_HOST")
        ?: throw IllegalStateException("WEBDAV_HOST not set")
    val user = System.getenv("WEBDAV_USER")
        ?: throw IllegalStateException("WEBDAV_USER not set")
    val pass = System.getenv("WEBDAV_PASS")
        ?: throw IllegalStateException("WEBDAV_PASS not set")
    Authenticator.setDefault(object : Authenticator() {
        override fun getPasswordAuthentication() =
            PasswordAuthentication(user, pass.toCharArray())
    })
    HttpURLConnection.setFollowRedirects(true)
    val url = URL(host.trimEnd('/') + "/$key")
    val conn = url.openConnection() as HttpURLConnection
    conn.requestMethod = "GET"
    conn.connect()
    if (conn.responseCode !in 200..299) throw IllegalStateException("Fallback download failed: HTTP ${conn.responseCode}")
    return conn.inputStream.use { it.readAllBytes() }
}

fun main(): Unit = runBlocking {
    val client = WebDavClient()
    val data = try {
        client.awaitFile("lobby.zip")
    } catch (_: ProtocolException) {
        downloadFallback("lobby.zip")
    }
    FileOutputStream("lobby.zip").use { it.write(data) }
    unzip("lobby.zip", "lobby")
    startServer()
}