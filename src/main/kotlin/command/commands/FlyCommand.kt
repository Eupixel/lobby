package net.eupixel.command.commands

import net.eupixel.save.Config.translator
import net.eupixel.vivlib.util.Permissions
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player

class FlyCommand : Command("fly") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val enabled = !sender.isAllowFlying
                sender.isAllowFlying = enabled
                sender.isFlying = enabled
                if (enabled) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(translator.get("flight_true", sender.locale)))
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(translator.get("flight_false", sender.locale)))
                }
            }
        }
        condition = CommandCondition { sender, _ ->
            sender is Player && Permissions.hasPermission(sender.uuid, "command.fly")
        }
    }
}