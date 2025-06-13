package net.eupixel.event.events

import net.eupixel.vivlib.core.WhitelistManager
import net.eupixel.save.Config
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent) {
    init {
        WhitelistManager.handle(event)
        event.spawningInstance = Config.instance
        event.player.respawnPoint = Config.spawnPosition
    }
}