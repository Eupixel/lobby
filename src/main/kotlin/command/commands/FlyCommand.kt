package net.eupixel.command.commands

import net.eupixel.save.saves.Messages
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player
import util.Permissions

class FlyCommand : Command("fly") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val enabled = !sender.isAllowFlying
                sender.isAllowFlying = enabled
                sender.isFlying = enabled
                sender.sendMessage(MiniMessage.miniMessage().deserialize(Messages.flight_state.replace("<state>", enabled.toString())))
            } else {
                sender.sendMessage("Only players can use this command.")
            }
        }
        condition = CommandCondition { sender, _ ->
            Permissions.hasPermission(sender as Player, "command.fly")
        }
    }
}