package 

import io.lumine.mythic.api.adapters.AbstractItemStack
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.items.ItemExecutor
import io.lumine.mythic.core.items.MythicItem
import org.bukkit.inventory.ItemStack

object MythicUtils {
    private val MMBukkit: MythicBukkit = MythicBukkit.inst()
    private val itemManager: ItemExecutor = MMBukkit.itemManager

    fun getMythicItem(itemName: String?): MythicItem {
        val mythicItem = itemManager.getItem(itemName)
        return mythicItem.orElse(null) as MythicItem
    }

    fun getMythicItemID(itemStack: ItemStack?): String {
        return itemManager.getMythicTypeFromItem(itemStack)
    }

    fun isMythicItem(itemStack: ItemStack?): Boolean {
        return itemManager.isMythicItem(itemStack)
    }

    fun getBukkitItem(itemStack: AbstractItemStack?): ItemStack {
        return BukkitAdapter.adapt(itemStack)
    }
}