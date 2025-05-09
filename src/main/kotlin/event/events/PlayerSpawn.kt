package net.eupixel.event.events

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            it.sendPacket(ParticlePacket(Particle.fromKey("enchanted_hit")!!, event.player.position, Pos(0.0, 2.0, 0.0), 1f,  1000))
        }
        event.player.gameMode = GameMode.CREATIVE
    }
}