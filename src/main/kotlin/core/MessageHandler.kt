package net.eupixel.core

import net.eupixel.save.Config
import net.eupixel.vivlib.core.DBTranslator
import net.eupixel.vivlib.core.Messenger
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.network.packet.server.common.TransferPacket

object MessageHandler {
    fun start() {
        Messenger.bind("0.0.0.0", 2905)
        Messenger.registerTarget("entrypoint", "entrypoint", 2905)
        Messenger.addRequestHandler("player_online", this::playerOnline)
        Messenger.addListener("queue_join", this::queueJoin)
        Messenger.addListener("queue_leave", this::queueLeave)
        Messenger.addListener("transfer", this::transfer)
        Messenger.addListener("message", this::message)
        Messenger.addListener("action_bar", this::actionBar)
        println("MessageHandler is now running!")
    }

    fun playerOnline(msg: String): String {
        return (MinecraftServer.getConnectionManager().findOnlinePlayer(msg) != null).toString()
    }

    fun queueJoin(msg: String) {
        val username = msg.split("&")[0]
        if(!Config.queued.contains(username)) {
            Config.queued.add(username)
        }
    }

    fun queueLeave(player: String) {
        Config.queued.remove(player)
    }

    fun transfer(msg: String) {
        val name = msg.split("?")[0]
        val target = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendPacket(TransferPacket(target.split("&")[0], target.split("&")[1].toInt()))
            }
        }
    }

    fun message(msg: String) {
        val name = msg.split("?")[0]
        val key = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendMessage(MiniMessage.miniMessage().deserialize(DBTranslator.translate(key, it.locale)))
            }
        }
    }

    fun actionBar(msg: String) {
        val name = msg.split("?")[0]
        val key = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendActionBar(MiniMessage.miniMessage().deserialize(DBTranslator.translate(key, it.locale)))
            }
        }
    }
}