package net.eupixel.command.commands

import net.eupixel.save.SaveManager
import net.eupixel.core.PrefixLoader
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player
import net.eupixel.vivlib.util.Permissions

class ReloadCommand : Command("reload") {
    init {
        setDefaultExecutor { sender, _ ->
            SaveManager.init()
            MinecraftServer.getConnectionManager().onlinePlayers.forEach {
                it.refreshCommands()
                PrefixLoader.loadPrefix(it)
            }
            sender.sendMessage(MiniMessage.miniMessage().deserialize("Reloaded!"))
        }
        condition = CommandCondition { sender, _ ->
            if (sender is Player) {
                Permissions.hasPermission(sender.uuid, "command.reload")
            } else {
                return@CommandCondition true
            }
        }
    }
}