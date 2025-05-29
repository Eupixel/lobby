package net.eupixel.save

import net.eupixel.core.DBTranslator
import net.eupixel.core.DirectusClient.getData
import net.eupixel.vivlib.util.Helper.convertToPos
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import net.minestom.server.instance.Instance
import kotlin.text.toDouble
import kotlin.text.toInt

object Config {
    var minY: Int = 0
    var spawnPosition: Pos = Pos(0.0, 0.0, 0.0)
    var availableGamemodes: List<String> = listOf()
    lateinit var instance: Instance
    lateinit var translator: DBTranslator
    private lateinit var title_entity: Entity

    fun init() {
        minY = getData("lobby_values", "name", "min_y", "data")?.toInt()?: 0
        instance.time = getData("lobby_values", "name", "time", "data")?.toLong()?: 1000
        spawnPosition = convertToPos(getData("lobby_values", "name", "spawn_position", "data"))
        availableGamemodes = getData("lobby_values", "name", "available_gamemodes", "data").orEmpty().split(":").toList()
        val title = getData("lobby_values", "name", "title", "data")
        val title_position = getData("lobby_values", "name", "title_position", "data")
        val title_size = getData("lobby_values", "name", "title_size", "data")
        val title_background = getData("lobby_values", "name", "title_background", "data")
        if (::title_entity.isInitialized) {
            title_entity.remove()
        }
        if(title != null && title_position != null && title_size != null && title_background != null) {
            title_entity = Entity(EntityType.TEXT_DISPLAY).apply {
                setInstance(instance, convertToPos(title_position))
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