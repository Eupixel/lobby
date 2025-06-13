package net.eupixel.event

import net.eupixel.event.events.AsyncPlayerConfiguration
import net.eupixel.event.events.InventoryPreClick
import net.eupixel.event.events.ItemDrop
import net.eupixel.event.events.PlayerBlockBreak
import net.eupixel.event.events.PlayerMove
import net.eupixel.event.events.PlayerSpawn
import net.eupixel.event.events.PlayerSwapItem
import net.eupixel.event.events.PlayerUseItem
import net.minestom.server.MinecraftServer
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent

object EventManager {
    fun init() {
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java, ::AsyncPlayerConfiguration)
        MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent::class.java, ::InventoryPreClick)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockBreakEvent::class.java, ::PlayerBlockBreak)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSwapItemEvent::class.java, ::PlayerSwapItem)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerUseItemEvent::class.java, ::PlayerUseItem)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent::class.java, ::PlayerSpawn)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent::class.java, ::PlayerMove)
        MinecraftServer.getGlobalEventHandler().addListener(ItemDropEvent::class.java, ::ItemDrop)
    }
}