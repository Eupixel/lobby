package net.eupixel.event.events

import kotlinx.coroutines.runBlocking
import net.eupixel.vivlib.util.DirectusClient
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent, instanceContainer: InstanceContainer) {
    init {
        val player = event.player
        event.spawningInstance = instanceContainer

        val spawn = runBlocking {
            val raw = DirectusClient.getSpawnPosition("lobby")
            if (raw != null && raw.contains("#")) {
                val parts = raw.split("#")
                if (parts.size == 5) {
                    try {
                        val (x, y, z, yaw, pitch) = parts
                        Pos(x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())
                    } catch (_: Exception) {
                        Pos.ZERO
                    }
                } else {
                    Pos.ZERO
                }
            } else {
                Pos.ZERO
            }
        }

        player.respawnPoint = spawn
    }
}