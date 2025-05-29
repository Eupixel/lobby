package net.eupixel.command.commands

import net.eupixel.save.Config.translator
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import java.util.Locale

class WhereAmICommand : Command("whereami") {
    init {
        setDefaultExecutor { sender, _ ->
            if(sender is Player) {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(translator.get("whereami", sender.locale).replace("<id>", System.getenv("HOSTNAME")
                    ?: runCatching { java.io.File("/etc/hostname").readText().trim() }.getOrNull()
                    ?: runCatching { java.net.InetAddress.getLocalHost().hostName }.getOrNull()
                    ?: "ID not found")))
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(translator.get("whereami", Locale.US).replace("<id>", System.getenv("HOSTNAME")
                    ?: runCatching { java.io.File("/etc/hostname").readText().trim() }.getOrNull()
                    ?: runCatching { java.net.InetAddress.getLocalHost().hostName }.getOrNull()
                    ?: "ID not found")))
            }
        }
    }
}