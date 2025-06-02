package net.eupixel.core

import java.util.UUID

object QueueManager {
    val queued = mutableSetOf<UUID>()
}