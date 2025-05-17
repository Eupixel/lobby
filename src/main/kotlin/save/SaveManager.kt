package net.eupixel.save

import kotlinx.coroutines.runBlocking
import net.eupixel.save.saves.Config
import net.eupixel.save.saves.Messages
import net.eupixel.vivlib.util.DirectusClient.getData

object SaveManager {
    fun init() {
        runBlocking {
            val minY = getData("lobby_values", "name", "min_y", listOf("data"))
                ?.get("data")
                ?.asInt()
            if (minY != null) {
                Config.minY = minY
            }

            val flight_state = getData("messages", "name", "flight_state", listOf("message"))
                ?.get("message")
                ?.asText()
            if (flight_state != null) {
                if(flight_state.contains("<prefix>")) {
                    val prefix = getData("messages", "name", "prefix", listOf("message"))
                        ?.get("message")
                        ?.asText()
                    Messages.flight_state = flight_state.replace("<prefix>", prefix.orEmpty())
                } else {
                    Messages.flight_state = flight_state
                }
            }
        }
    }
}