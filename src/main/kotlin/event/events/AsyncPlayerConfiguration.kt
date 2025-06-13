package net.eupixel.event.events

import net.eupixel.save.Config
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent) {
    init {
        event.spawningInstance = Config.instance
        event.player.respawnPoint = Config.spawnPosition
    }
}