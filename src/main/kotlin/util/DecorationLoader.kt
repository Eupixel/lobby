package net.eupixel.util

import kotlinx.coroutines.runBlocking
import net.eupixel.vivlib.util.DirectusClient
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.TextDisplayMeta

object DecorationLoader {
    fun init() {
        var title: String?
        var title_position: String?
        var title_size: String?
        var title_background: String?

        runBlocking {
            title = DirectusClient.getData("lobby_values", "name", "title", listOf("data"))
                ?.get("data")
                ?.asText()
            title_position = DirectusClient.getData("lobby_values", "name", "title_position", listOf("data"))
                ?.get("data")
                ?.asText()
            title_size = DirectusClient.getData("lobby_values", "name", "title_size", listOf("data"))
                ?.get("data")
                ?.asText()
            title_background = DirectusClient.getData("lobby_values", "name", "title_background", listOf("data"))
                ?.get("data")
                ?.asText()

            println("title=$title")
            println("title_position=$title_position")
            println("title_size=$title_size")
            println("title_background=$title_background")
        }

        if(title != null && title_position != null && title_size != null && title_background != null) {
            val title_entity = Entity(EntityType.TEXT_DISPLAY).apply {
                setInstance(Config.instance, Util.convertToPos(title_position))
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