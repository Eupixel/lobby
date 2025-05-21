package net.eupixel.event

import net.eupixel.event.events.AsyncPlayerConfiguration
import net.eupixel.event.events.PlayerBlockBreak
import net.eupixel.event.events.PlayerChat
import net.eupixel.event.events.PlayerDisconnect
import net.eupixel.event.events.PlayerMove
import net.eupixel.event.events.PlayerSpawn
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent

object EventManager {
    fun init() {
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java, ::AsyncPlayerConfiguration)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockBreakEvent::class.java, ::PlayerBlockBreak)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent::class.java, ::PlayerDisconnect)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent::class.java, ::PlayerSpawn)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent::class.java, ::PlayerMove)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent::class.java, ::PlayerChat)
    }
}