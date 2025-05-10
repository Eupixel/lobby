package net.eupixel.event.events

import net.minestom.server.event.player.PlayerBlockBreakEvent

class PlayerBlockBreak(event: PlayerBlockBreakEvent) {
    init {
        event.isCancelled = true
    }
}