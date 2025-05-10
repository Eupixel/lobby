package net.eupixel.event.events

import kotlinx.coroutines.runBlocking
import net.eupixel.vivlib.util.WebDavClient
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent, instanceContainer: InstanceContainer) {
    init {
        val player = event.player
        event.spawningInstance = instanceContainer
        val lobbySpawn = runBlocking {
            WebDavClient().awaitValue("lobby_spawn")
        }?: "none"
        val spawn = if (lobbySpawn != "none") {
            val parts = lobbySpawn.split("#")
            val vals = parts.map { it.toDouble() }
            Pos(vals[0], vals[1], vals[2], vals[3].toFloat(), vals[4].toFloat())
        } else {
            Pos.ZERO
        }
        player.respawnPoint = spawn
    }
}