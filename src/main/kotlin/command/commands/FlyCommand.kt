package net.eupixel.command.commands

import net.eupixel.save.saves.Messages
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
                sender.sendMessage(MiniMessage.miniMessage().deserialize(Messages.flight_state.replace("<state>", enabled.toString())))
            }
        }
        condition = CommandCondition { sender, _ ->
            if (sender is Player) {
                Permissions.hasPermission(sender.uuid, "command.fly")
            } else {
                return@CommandCondition false
            }
        }
    }
}