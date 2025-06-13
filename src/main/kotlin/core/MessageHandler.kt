package net.eupixel.core

import net.eupixel.save.Config
import net.eupixel.vivlib.core.Messenger

object MessageHandler {
    fun start() {
        Messenger.addListener("queue_join", this::queueJoin)
        Messenger.addListener("queue_leave", this::queueLeave)
        println("MessageHandler is now running!")
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
}