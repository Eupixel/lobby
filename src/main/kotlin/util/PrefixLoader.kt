package net.eupixel.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import util.Permissions

object PrefixLoader {
    fun loadPrefix(player: Player) {
        MinecraftServer.getTeamManager().deleteTeam(player.username)
        val prefix = Permissions.getPrefix(player) + " "
        player.team = MinecraftServer.getTeamManager().createTeam(player.username, MiniMessage.miniMessage().deserialize(prefix),
            NamedTextColor.WHITE, Component.empty())
    }
}