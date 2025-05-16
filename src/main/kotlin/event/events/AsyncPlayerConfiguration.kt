package net.eupixel.event.events

import kotlinx.coroutines.runBlocking
import net.eupixel.util.Util.convertToPos
import net.eupixel.util.Config
import net.eupixel.vivlib.util.DirectusClient.getData
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent) {
    init {
        event.spawningInstance = Config.instance
        Config.spawnPosition = runBlocking {
            val raw = getData("lobby_values", "name", "spawn_position", listOf("data"))
                ?.get("data")
                ?.asText()
            convertToPos(raw)
        }
        event.player.respawnPoint = Config.spawnPosition
    }
}