package net.eupixel.save

import kotlinx.coroutines.runBlocking
import net.eupixel.core.DirectusClient.getData
import net.eupixel.save.saves.Config
import net.eupixel.vivlib.util.Helper.convertToPos
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.TextDisplayMeta

object SaveManager {
    private lateinit var title_entity: Entity
    fun init() {
        var title: String?
        var title_position: String?
        var title_size: String?
        var title_background: String?
        runBlocking {
            Config.minY = getData("lobby_values", "name", "min_y", listOf("data"))
                ?.get("data")
                ?.asInt(0)?: 0
            Config.time = getData("lobby_values", "name", "time", listOf("data"))
                ?.get("data")
                ?.asLong(1000)?: 1000
            Config.spawnPosition = runBlocking {
                val raw = getData("lobby_values", "name", "spawn_position", listOf("data"))
                    ?.get("data")
                    ?.asText()
                convertToPos(raw)
            }
            title = getData("lobby_values", "name", "title", listOf("data"))
                ?.get("data")
                ?.asText()
            title_position = getData("lobby_values", "name", "title_position", listOf("data"))
                ?.get("data")
                ?.asText()
            title_size = getData("lobby_values", "name", "title_size", listOf("data"))
                ?.get("data")
                ?.asText()
            title_background = getData("lobby_values", "name", "title_background", listOf("data"))
                ?.get("data")
                ?.asText()
        }
        Config.instance.time = Config.time
        if (::title_entity.isInitialized) {
            title_entity.remove()
        }
        if(title != null && title_position != null && title_size != null && title_background != null) {
            title_entity = Entity(EntityType.TEXT_DISPLAY).apply {
                setInstance(Config.instance, convertToPos(title_position))
                setNoGravity(true)
            }
            val title_meta = title_entity.entityMeta as TextDisplayMeta
            title_meta.apply {
                text = MiniMessage.miniMessage().deserialize(title)
                scale = Vec(title_size.toDouble())
                backgroundColor = title_size.toInt()
                isHasGlowingEffect = true
            }
        }
    }
}