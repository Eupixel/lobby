package net.eupixel.event.events

import kotlinx.coroutines.runBlocking
import net.eupixel.util.PocketBaseFileClient
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        val (key, speed, amount) = runBlocking {
            val client = PocketBaseFileClient()
            val k = client.getValueAwait("values", "particle_key")
            val s = client.getValueAwait("values", "particle_speed").toFloat()
            val a = client.getValueAwait("values", "particle_amount").toInt()
            Triple(k, s, a)
        }

        val particle = Particle.fromKey(key) ?: Particle.ENCHANTED_HIT
        MinecraftServer.getConnectionManager().onlinePlayers.forEach { player ->
            player.sendPacket(
                ParticlePacket(
                    particle,
                    event.player.position,
                    Pos(0.0, 2.0, 0.0),
                    speed,
                    amount
                )
            )
        }
    }
}