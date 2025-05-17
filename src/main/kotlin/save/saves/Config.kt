package net.eupixel.save.saves

import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance

object Config {
    var minY: Int = 0
    var spawnPosition: Pos = Pos(0.0, 0.0, 0.0)
    lateinit var instance: Instance
}