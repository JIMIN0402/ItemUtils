package 

import net.Indyuce.mmoitems.MMOItems
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object MMOUtils {
    private val MMO: MMOItems = MMOItems.plugin

    fun getType(itemStack: ItemStack?): String? {
        return MMOItems.getTypeName(itemStack)
    }

    fun getID(itemStack: ItemStack?): String? {
        return MMOItems.getID(itemStack)
    }

    fun getNamespaceID(itemStack: ItemStack?): String {
        val var10000 = getType(itemStack)
        return var10000 + ":" + getID(itemStack)
    }

    fun getMMOItemsItem(type: String?, id: String?): ItemStack {
        val types = MMO.types
        if (!types.has(type)) {
            return ItemStack(Material.AIR)
        } else {
            val mmoItem = MMO.getMMOItem(types[type], id)
            return if (mmoItem == null) ItemStack(Material.AIR) else mmoItem.newBuilder().build()!!
        }
    }
}