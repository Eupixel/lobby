package net.eupixel.event.events

import net.minestom.server.event.item.ItemDropEvent

class ItemDrop(event: ItemDropEvent) {
    init {
        event.isCancelled = true
    }
}