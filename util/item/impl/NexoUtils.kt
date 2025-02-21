package 

import com.nexomc.nexo.api.NexoItems.builderFromItem
import com.nexomc.nexo.api.NexoItems.exists
import com.nexomc.nexo.api.NexoItems.idFromItem
import com.nexomc.nexo.api.NexoItems.itemFromId
import com.nexomc.nexo.items.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object NexoUtils {
    fun getItemBuilder(itemID: String?): ItemBuilder? {
        return itemFromId(itemID)
    }

    fun getItemBuilder(itemStack: ItemStack?): ItemBuilder? {
        return builderFromItem(itemStack)
    }

    fun getID(itemStack: ItemStack?): String? {
        return idFromItem(itemStack)
    }

    fun getNexoItem(itemID: String?): ItemStack {
        val itemBuilder = getItemBuilder(itemID)
        return itemBuilder?.build() ?: ItemStack(Material.AIR)
    }

    fun isNexoItem(itemStack: ItemStack?): Boolean {
        return exists(itemStack)
    }
}