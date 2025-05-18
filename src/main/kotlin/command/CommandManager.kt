package net.eupixel.command

import net.eupixel.command.commands.FlyCommand
import net.eupixel.command.commands.ReloadCommand
import net.minestom.server.MinecraftServer

object CommandManager {
    fun init() {
        MinecraftServer.getCommandManager().register(FlyCommand())
        MinecraftServer.getCommandManager().register(ReloadCommand())
    }
}