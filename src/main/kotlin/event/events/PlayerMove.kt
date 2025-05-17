package net.eupixel.event.events

import net.eupixel.save.saves.Config
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMove(event: PlayerMoveEvent) {
    init {
        if(event.player.position.y < Config.minY) {
            event.player.teleport(event.player.respawnPoint)
        }
    }
}