package net.eupixel.core

import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.network.packet.server.common.TransferPacket

object MessageHandler {
    fun start() {
        Messenger.bind("0.0.0.0", 2905)
        Messenger.registerTarget("entrypoint", "entrypoint", 2905)
        Messenger.addListener("transfer", this::transfer)
        Messenger.addListener("message", this::message)
        Messenger.addListener("action_bar", this::actionBar)
        println("MessageHandler is now running!")
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
        val msg = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendMessage(MiniMessage.miniMessage().deserialize(msg))
            }
        }
    }

    fun actionBar(msg: String) {
        val name = msg.split("?")[0]
        val msg = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendActionBar(MiniMessage.miniMessage().deserialize(msg))
            }
        }
    }
}