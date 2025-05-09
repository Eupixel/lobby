package net.eupixel.event.events

import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent, instanceContainer: InstanceContainer) {
    init {
        val player = event.player
        event.spawningInstance = instanceContainer
        player.respawnPoint = Pos(0.0, 200.0, 0.0)
    }
}