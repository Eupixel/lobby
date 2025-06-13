package net.eupixel.command.commands

import net.eupixel.vivlib.core.DBTranslator
import net.eupixel.vivlib.core.Messenger
import net.eupixel.save.Config
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
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
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val locale = sender.locale
                sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_usage", locale)))
            }
        }
        addSyntax({ sender, _ ->
            if (sender is Player) {
                val locale = sender.locale
                if (!Config.queued.contains(sender.uuid.toString())) {
                    sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_none", locale)))
                } else {
                    sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_left", locale)))
                    Messenger.send("entrypoint", "queue_leave_request", sender.uuid.toString())
                }
            }
        }, action)
        addSyntax({ sender, context ->
            if (sender is Player) {
                val locale = sender.locale
                if (!context.get(action).equals("join", ignoreCase = true)) {
                    sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_usage", locale)))
                    return@addSyntax
                }
                if (Config.queued.contains(sender.uuid.toString())) {
                    sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_already", locale)))
                    return@addSyntax
                }
                val gm = context.get(gamemode)
                if (!Config.availableGamemodes.contains(gm)) {
                    sender.sendMessage(miniMessage().deserialize(DBTranslator.get("invalid_gamemode", locale).replace("<input>", gm)))
                    return@addSyntax
                }
                sender.sendMessage(miniMessage().deserialize(DBTranslator.get("queue_joined", locale).replace("<gamemode>", gm)))
                Messenger.send("entrypoint", "queue_join_request", "${sender.uuid}&$gm")
            }
        }, action, gamemode)
        condition = CommandCondition { sender, _ -> sender is Player }
    }
}