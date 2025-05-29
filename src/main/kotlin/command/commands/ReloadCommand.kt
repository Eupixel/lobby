package net.eupixel.command.commands

import net.eupixel.save.Config
import net.eupixel.util.PrefixLoader
import net.eupixel.vivlib.util.Permissions
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player
import java.util.Locale
import kotlin.system.measureTimeMillis

class ReloadCommand : Command("reload") {
    init {
        setDefaultExecutor { sender, _ ->
            sender.sendMessage(MiniMessage.miniMessage().deserialize("Reloading..."))
            val ms = measureTimeMillis {
                Config.init()
                Config.translator.loadFromDB()
                Permissions.refreshAll()
                MinecraftServer.getConnectionManager().onlinePlayers.forEach {
                    it.refreshCommands()
                    PrefixLoader.loadPrefix(it)
                }
            }
            val formatted = String.format(Locale.US, "%.2f", ms / 1000.0)
            sender.sendMessage(MiniMessage.miniMessage().deserialize("Reloaded! (in $formatted s)"))
        }
        condition = CommandCondition { sender, _ ->
            sender is Player && Permissions.hasPermission(sender.uuid, "command.reload")
        }
    }
}