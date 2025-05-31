package net.eupixel.event.events

import net.eupixel.core.Messenger
import net.eupixel.qm
import net.minestom.server.event.player.PlayerDisconnectEvent

class PlayerDisconnect(event: PlayerDisconnectEvent) {
    init {
        event.player.passengers.forEach { it.remove() }
        qm.queued.remove(event.player.uuid)
        Messenger.send("entrypoint", "queue_left", event.player.username)
    }
}