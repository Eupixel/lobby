package net.eupixel.save

import net.eupixel.vivlib.core.DirectusClient.getData
import net.eupixel.vivlib.core.DirectusClient.listItems
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
    lateinit var instance: Instance
    val queued: MutableSet<String> = mutableSetOf()

    var navigatorType: String = ""
    var playerTTL: Int = 30
    var heldItem: Byte = 4
    var minY: Int = 0
    var spawnPosition: Pos = Pos(0.0, 0.0, 0.0)
    var availableGamemodes: List<String> = listOf()
    private lateinit var titleEntity: Entity

    fun init() {
        navigatorType = getData("lobby_values", "name", "navigator_type", "data").orEmpty()
        playerTTL = getData("global_values", "name", "player_ttl", "data")?.toInt()?: 30
        heldItem = getData("lobby_values", "name", "held_item", "data")?.toByte() ?: 4
        minY = getData("lobby_values", "name", "min_y", "data")?.toInt() ?: 0
        spawnPosition = convertToPos(getData("lobby_values", "name", "spawn_position", "data"))
        availableGamemodes = listItems("gamemodes", "friendly_name")

        instance.time = getData("lobby_values", "name", "time", "data")?.toLong() ?: 1000

        val title = getData("lobby_values", "name", "title", "data")
        val titlePosition = getData("lobby_values", "name", "title_position", "data")
        val titleSize = getData("lobby_values", "name", "title_size", "data")
        val titleBg = getData("lobby_values", "name", "title_background", "data")

        if (::titleEntity.isInitialized) {
            titleEntity.remove()
        }

        if (title != null && titlePosition != null && titleSize != null && titleBg != null) {
            titleEntity = Entity(EntityType.TEXT_DISPLAY).apply {
                setInstance(this@Config.instance, convertToPos(titlePosition))
                setNoGravity(true)
            }
            val meta = titleEntity.entityMeta as TextDisplayMeta
            meta.text = MiniMessage.miniMessage().deserialize(title)
            meta.scale = Vec(titleSize.toDouble())
            meta.backgroundColor = titleBg.removePrefix("0x").toInt(16)
            meta.isHasGlowingEffect = true
        }
    }
}