package net.eupixel.event.events

import kotlinx.coroutines.runBlocking
import net.eupixel.vivlib.util.DirectusClient.getData
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent, instanceContainer: InstanceContainer) {
    init {
        event.spawningInstance = instanceContainer
        val spawn = runBlocking {
            val raw = getData("worlds", "name", "lobby", listOf("spawn_position"))
                ?.get("spawn_position")
                ?.asText()
            if (raw != null && raw.contains("#")) {
                val parts = raw.split("#")
                if (parts.size == 5) {
                    val x     = parts[0].toDouble()
                    val y     = parts[1].toDouble()
                    val z     = parts[2].toDouble()
                    val yaw   = parts[3].toFloat()
                    val pitch = parts[4].toFloat()
                    Pos(x, y, z, yaw, pitch)
                } else {
                    Pos.ZERO
                }
            } else {
                Pos.ZERO
            }
        }
        event.player.respawnPoint = spawn
    }
}