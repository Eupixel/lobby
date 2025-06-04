package net.eupixel.command.commands

import net.eupixel.core.Messenger
import net.eupixel.save.Config
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player

class QueueCommand : Command("queue") {
    init {
        val action = ArgumentType.Word("action")
        action.setSuggestionCallback { sender, _, suggestions ->
            if (sender is Player) {
                suggestions.addEntry(SuggestionEntry("join"))
                suggestions.addEntry(SuggestionEntry("leave"))
            }
        }
        val gamemode = ArgumentType.Word("gamemode")
        gamemode.setSuggestionCallback { sender, context, suggestions ->
            if (sender is Player && context.get(action).equals("join", ignoreCase = true)) {
                Config.availableGamemodes.forEach { suggestions.addEntry(SuggestionEntry(it)) }
            }
        }
        addSyntax({ sender, context ->
            if ((sender as Player).let { context.get(action).equals("leave", ignoreCase = true) })
                Messenger.send("entrypoint", "queue_leave_request", sender.username)
        }, action)
        addSyntax({ sender, context ->
            if ((sender as Player).let { context.get(action).equals("join", ignoreCase = true) })
                Messenger.send("entrypoint", "queue_join_request", "${sender.username}&${context.get(gamemode)}")
        }, action, gamemode)
        condition = CommandCondition { sender, _ -> sender is Player }
    }
}