package net.eupixel.event.events

import net.eupixel.feature.Navigator
import net.eupixel.save.Config.heldItem
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        event.player.inventory.clear()
        event.player.setHeldItemSlot(heldItem)
        Navigator.give(event.player)
    }
}