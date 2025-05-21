package net.eupixel.event.events

import net.eupixel.save.saves.Config.translator
import net.eupixel.vivlib.util.Permissions
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.minestom.server.event.player.PlayerChatEvent
import java.util.Locale

class PlayerChat(event: PlayerChatEvent) {
    init {
        val format = translator.translate("chat", Locale.US).toPattern()
        var message = format
            .replace("<player_prefix>", Permissions.getPrefix(event.player.uuid))
            .replace("<player_name>", event.player.username)
            .replace("<message>", event.rawMessage)
        event.formattedMessage = miniMessage().deserialize(message)
    }
}