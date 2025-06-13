package net.eupixel.event.events

import net.eupixel.feature.Navigator
import net.eupixel.save.Config.heldItem
import net.eupixel.vivlib.util.PrefixLoader.loadPrefix
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        loadPrefix(event.player)
        event.player.inventory.clear()
        event.player.setHeldItemSlot(heldItem)
        Navigator.give(event.player)
    }
}