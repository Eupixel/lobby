package net.eupixel.core

import java.util.UUID

class QueueManager {
    val queued = mutableSetOf<UUID>()
}