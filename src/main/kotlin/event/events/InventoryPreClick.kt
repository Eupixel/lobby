package net.eupixel.event.events

import net.eupixel.feature.Navigator
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.tag.Tag

class InventoryPreClick(event: InventoryPreClickEvent) {
    init {
        event.isCancelled = true
        if(event.player.inventory.cursorItem.getTag(Tag.String("navigator")) != null) {
            Navigator.click(event.player)
        }
    }
}