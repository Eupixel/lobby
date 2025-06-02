package net.eupixel.command.commands

import net.eupixel.core.DBTranslator
import net.eupixel.core.Messenger
import net.eupixel.core.QueueManager
import net.eupixel.save.Config
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.command.builder.condition.CommandCondition
import net.minestom.server.entity.Player

class QueueCommand : Command("queue") {
    private val mini = MiniMessage.miniMessage()

    init {
        val action = ArgumentType.Word("action")
        action.setSuggestionCallback { sender, _, suggestions ->
            if (sender is Player) {
                suggestions.addEntry(SuggestionEntry("join"))
                suggestions.addEntry(SuggestionEntry("leave"))
            }
        }
        val gamemodeArg = ArgumentType.Word("gamemode")
        gamemodeArg.setSuggestionCallback { sender, context, suggestions ->
            if (sender is Player && context.get(action).equals("join", ignoreCase = true)) {
                Config.availableGamemodes.forEach { suggestions.addEntry(SuggestionEntry(it)) }
            }
        }
        gamemodeArg.setCallback { sender, exception ->
            if (sender is Player) {
                val locale = sender.locale
                sender.sendMessage(mini.deserialize(DBTranslator.get("invalid_gamemode", locale).replace("<input>", exception.input)))
            }
        }
        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                val locale = sender.locale
                sender.sendMessage(mini.deserialize(DBTranslator.get("queue_usage", locale)))
            }
        }
        addSyntax({ sender, _ ->
            if (sender is Player) {
                val locale = sender.locale
                if (!QueueManager.queued.contains(sender.uuid)) {
                    sender.sendMessage(mini.deserialize(DBTranslator.get("queue_none", locale)))
                    return@addSyntax
                }
                QueueManager.queued.remove(sender.uuid)
                sender.sendMessage(mini.deserialize(DBTranslator.get("queue_left", locale)))
                Messenger.send("entrypoint", "queue_left", sender.username)
            }
        }, action)
        addSyntax({ sender, context ->
            if (sender is Player) {
                val locale = sender.locale
                val act = context.get(action)
                if (!act.equals("join", ignoreCase = true)) {
                    sender.sendMessage(mini.deserialize(DBTranslator.get("queue_usage", locale)))
                    return@addSyntax
                }
                if (QueueManager.queued.contains(sender.uuid)) {
                    sender.sendMessage(mini.deserialize(DBTranslator.get("queue_already", locale)))
                    return@addSyntax
                }
                val gm = context.get(gamemodeArg)
                if (!Config.availableGamemodes.contains(gm)) {
                    sender.sendMessage(mini.deserialize(DBTranslator.get("invalid_gamemode", locale).replace("<input>", gm)))
                    return@addSyntax
                }
                QueueManager.queued.add(sender.uuid)
                sender.sendMessage(mini.deserialize(DBTranslator.get("queue_joined", locale).replace("<gamemode>", gm)))
                Messenger.send("entrypoint", "queue_joined", "${sender.username}&$gm")
            }
        }, action, gamemodeArg)
        condition = CommandCondition { sender, _ -> sender is Player }
    }
}