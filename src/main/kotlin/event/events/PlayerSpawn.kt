package net.eupixel.event.events

import net.eupixel.core.PrefixLoader
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        PrefixLoader.loadPrefix(event.player)
    }
}