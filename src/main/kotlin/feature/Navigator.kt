package net.eupixel.feature

import net.eupixel.save.Config
import net.eupixel.vivlib.core.DBTranslator
import net.eupixel.vivlib.core.Messenger
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.network.packet.server.common.TransferPacket
import net.minestom.server.tag.Tag
import java.net.InetAddress

object Navigator {
    fun give(player: Player) {
        val item = Material.fromKey(Config.navigatorType)?.let { ItemStack
            .builder(it)
            .customName(miniMessage().deserialize(DBTranslator.translate("navigator_name", player.locale)).decoration(TextDecoration.ITALIC, false))
            .hideExtraTooltip()
            .set(Tag.String("navigator"), "")
            .build()
        }
        if (item != null) {
            player.inventory.setItemStack(4, item)
        }
    }

    fun use(player: Player) {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, miniMessage().deserialize(DBTranslator.translate("navigator_name", player.locale)))
        val serversRaw = Messenger.sendRequest("entrypoint", "lobby_list", "navigator")
        serversRaw?.let {
            val servers = it.split("#")
            var slot = 53
            servers.forEach { s ->
                val parts = s.split("&")
                val host = parts[0]
                val port = parts[1]
                val id = parts[2]
                val players = parts[3]
                val name = miniMessage().deserialize("Lobby-${id.take(4)}").decoration(TextDecoration.ITALIC, false)
                val baseLore = miniMessage().deserialize(DBTranslator.translate("players_on_lobby", player.locale).replace("<count>", players)).decoration(TextDecoration.ITALIC, false)
                var item = ItemStack.builder(Material.NETHER_STAR)
                    .customName(name)
                    .lore(baseLore)
                    .set(Tag.String("host"), "$host&$port")
                    .build()
                if(InetAddress.getLocalHost().hostName == id) {
                    item = item.withLore(listOf(
                        baseLore,
                        miniMessage().deserialize(DBTranslator.translate("current_lobby", player.locale)).decoration(TextDecoration.ITALIC, false)
                    ))
                }
                inventory.setItemStack(slot, item)
                slot--
            }
        }
        player.openInventory(inventory)
    }

    fun click(player: Player, item: ItemStack) {
        val tag = item.getTag(Tag.String("host"))
        player.sendPacket(TransferPacket(tag.split("&")[0], tag.split("&")[1].toInt()))
    }
}