package net.eupixel.core

import net.minestom.server.coordinate.Pos

object Util {
    fun convertToPos(raw: String?): Pos {
        if (raw.isNullOrBlank() || !raw.contains("#")) return Pos.ZERO
        val parts = raw.split("#")
        return try {
            when (parts.size) {
                3 -> {
                    val x = parts[0].toDouble()
                    val y = parts[1].toDouble()
                    val z = parts[2].toDouble()
                    Pos(x, y, z)
                }
                5 -> {
                    val x = parts[0].toDouble()
                    val y = parts[1].toDouble()
                    val z = parts[2].toDouble()
                    val yaw = parts[3].toFloat()
                    val pitch = parts[4].toFloat()
                    Pos(x, y, z, yaw, pitch)
                }
                else -> Pos.ZERO
            }
        } catch (_: NumberFormatException) {
            Pos.ZERO
        }
    }
}