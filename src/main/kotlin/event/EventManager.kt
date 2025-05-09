package net.eupixel.event

import net.eupixel.event.events.AsyncPlayerConfiguration
import net.eupixel.event.events.PlayerBlockBreak
import net.eupixel.event.events.PlayerSpawn
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.InstanceContainer

class EventManager(globalEventHandler: GlobalEventHandler, instanceContainer: InstanceContainer) {
    init {
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { AsyncPlayerConfiguration(it, instanceContainer) }
        globalEventHandler.addListener(PlayerBlockBreakEvent::class.java, ::PlayerBlockBreak)
        globalEventHandler.addListener(PlayerSpawnEvent::class.java, ::PlayerSpawn)
    }
}