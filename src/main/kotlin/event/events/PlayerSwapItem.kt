package net.eupixel.event.events

import net.minestom.server.event.player.PlayerSwapItemEvent

class PlayerSwapItem(event: PlayerSwapItemEvent) {
    init {
        event.isCancelled = true
    }
}