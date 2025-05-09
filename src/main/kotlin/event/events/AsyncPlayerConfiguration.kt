package net.eupixel.event.events

import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent, instanceContainer: InstanceContainer) {
    init {
        val player = event.player
        event.spawningInstance = instanceContainer
        var spawn = Pos.ZERO
        val lobbySpawn = System.getenv("LOBBY_SPAWN")?: "none"
        if(lobbySpawn != "none") {
            val parts = lobbySpawn.split("#")
            val vals = ArrayList<Double>()
            parts.forEach {
                vals.add(it.toDouble())
            }
            spawn = Pos(vals[0], vals[1], vals[2], vals[3].toFloat(), vals[4].toFloat())
        }
        player.respawnPoint = spawn
    }
}