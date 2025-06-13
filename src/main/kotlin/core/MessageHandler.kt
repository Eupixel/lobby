package net.eupixel.core

import net.eupixel.save.Config
import net.eupixel.vivlib.core.Messenger

object MessageHandler {
    fun start() {
        Messenger.addListener("queue_join", this::queueJoin)
        Messenger.addListener("queue_leave", this::queueLeave)
        println("MessageHandler is now running!")
    }

    fun queueJoin(uuid: String) {
        if(!Config.queued.contains(uuid)) {
            Config.queued.add(uuid)
        }
    }

    fun queueLeave(uuid: String) {
        Config.queued.remove(uuid)
    }
}