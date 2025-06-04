package net.eupixel.event.events

import net.minestom.server.event.player.PlayerDisconnectEvent

class PlayerDisconnect(event: PlayerDisconnectEvent) {
    init {
        event.player.passengers.forEach { it.remove() }
    }
}