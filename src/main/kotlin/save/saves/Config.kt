package net.eupixel.save.saves

import net.eupixel.core.DBTranslator
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance

object Config {
    var minY: Int = 0
    var time: Long = 1000
    var spawnPosition: Pos = Pos(0.0, 0.0, 0.0)
    lateinit var instance: Instance
    lateinit var translator: DBTranslator
}