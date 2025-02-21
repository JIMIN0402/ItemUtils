package 

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object IAUtils {
    fun getCustomStack(namespaceID: String): CustomStack? {
        return CustomStack.getInstance(namespaceID)
    }

    fun getCustomStack(itemStack: ItemStack?): CustomStack? {
        return CustomStack.byItemStack(itemStack)
    }

    fun getItemsAdderItem(namespaceID: String): ItemStack {
        val customStack = getCustomStack(namespaceID)
        return if (customStack == null) ItemStack(Material.AIR) else customStack.itemStack
    }
}