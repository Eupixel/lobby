package net.eupixel.event.events

import net.eupixel.save.Config
import net.eupixel.vivlib.util.Permissions
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.minestom.server.event.player.PlayerChatEvent

class PlayerChat(event: PlayerChatEvent) {
    init {
        val format = Config.chatFormat
        var message = format
            .replace("<player_prefix>", Permissions.getPrefix(event.player.uuid))
            .replace("<player>", event.player.username)
            .replace("<message>", event.rawMessage)
        event.formattedMessage = miniMessage().deserialize(message)
    }
}