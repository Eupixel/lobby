package net.eupixel.event.events


import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerSpawnEvent
import util.Permissions

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        val prefix = Permissions.getPrefix(event.player) + " "
        println(prefix)
        val team = MinecraftServer.getTeamManager().createTeam(event.player.username, MiniMessage.miniMessage().deserialize(prefix),
            NamedTextColor.WHITE, Component.empty())
        event.player.setTeam(team)
        println("Gave ${event.player.username} the prefix $prefix.")
    }
}