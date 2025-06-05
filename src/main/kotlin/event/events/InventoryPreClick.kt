package net.eupixel.event.events

import net.eupixel.feature.Navigator
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.tag.Tag

class InventoryPreClick(event: InventoryPreClickEvent) {
    init {
        event.isCancelled = true
        if(event.clickedItem.getTag(Tag.String("host")) != null) {
            Navigator.click(event.player, event.clickedItem)
        }
    }
}