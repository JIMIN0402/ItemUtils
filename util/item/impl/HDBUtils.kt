package 

import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack

object HDBUtils {
    private val HDB: HeadDatabaseAPI = HeadDatabaseAPI()

    fun getID(itemStack: ItemStack): String? {
        return HDB.getItemID(itemStack)
    }

    fun isHDBItem(itemStack: ItemStack): Boolean {
        return getID(itemStack) != null
    }

    fun getHDBItem(itemName: String): ItemStack {
        return HDB.getItemHead(itemName)
    }

}
