package net.eupixel.save

import kotlinx.coroutines.runBlocking
import net.eupixel.save.saves.Config
import net.eupixel.save.saves.Messages
import net.eupixel.vivlib.util.DirectusClient.getData

object SaveManager {
    fun init() {
        runBlocking {
            Config.minY = getData("lobby_values", "name", "min_y", listOf("data"))
                ?.get("data")
                ?.asInt(0)?: 0

            Messages.prefix = getData("messages", "name", "prefix", listOf("message"))
                ?.get("message")
                ?.asText().orEmpty()

            Messages.flight_state = getData("messages", "name", "flight_state", listOf("message"))
                ?.get("message")
                ?.asText().orEmpty()
                .replace("<prefix>", Messages.prefix)
        }
    }
}