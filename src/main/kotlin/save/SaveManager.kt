package net.eupixel.save

import kotlinx.coroutines.runBlocking
import net.eupixel.core.DirectusClient.getData
import net.eupixel.save.saves.Config
import net.eupixel.vivlib.util.Helper.convertToPos

object SaveManager {
    fun init() {
        runBlocking {
            Config.minY = getData("lobby_values", "name", "min_y", listOf("data"))
                ?.get("data")
                ?.asInt(0)?: 0
            Config.spawnPosition = runBlocking {
                val raw = getData("lobby_values", "name", "spawn_position", listOf("data"))
                    ?.get("data")
                    ?.asText()
                convertToPos(raw)
            }
        }
    }
}