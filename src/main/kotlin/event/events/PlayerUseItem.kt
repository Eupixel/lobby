package net.eupixel.event.events

import net.eupixel.feature.Navigator
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.tag.Tag

class PlayerUseItem(event: PlayerUseItemEvent) {
    init {
        if(event.itemStack.getTag(Tag.String("navigator")) != null) {
            Navigator.use(event.player)
        }
    }
}