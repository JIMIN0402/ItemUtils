package 

import de.tr7zw.nbtapi.NBT
import de.tr7zw.nbtapi.iface.ReadableNBT
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.items.MythicItem
import .impl.*
import kr.jimin.addon.addcrop.plugin.util.StringUtils
import kr.jimin.addon.addcrop.plugin.util.VersionUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object ItemUtils {

    fun getCustomItem(itemName: String): ItemStack {
        val itemType = itemName.split(":", limit = 2)[0]
        val itemValue = itemName.substring(itemName.indexOf(":") + 1)

        return when (itemType) {
            "ia" -> IAUtils.getItemsAdderItem(itemValue)
            "nexo" -> NexoUtils.getNexoItem(itemValue)
            "mm" -> getMythicItem(itemValue)
            "mmo" -> getMMOItemsItem(itemValue)
            "hdb" -> HDBUtils.getHDBItem(itemValue)
            else -> Material.matchMaterial(itemName.uppercase())?.let { ItemStack(it) } ?: ItemStack(Material.AIR)
        }
    }

    fun getCustomItemType(itemStack: ItemStack): String {
        return when {
            hasNBT(itemStack, "itemsadder") -> "ia"
            hasPDC(itemStack, "nexo", "id") -> "nexo"
            MythicUtils.isMythicItem(itemStack) -> "mm"
            hasNBT(itemStack, "MMOITEMS_ITEM_TYPE") -> "mmo"
            HDBUtils.isHDBItem(itemStack) -> "hdb"
            else -> "vanilla"
        }
    }

    fun getCustomItemId(itemStack: ItemStack): String {
        return when {
            hasNBT(itemStack, "itemsadder") -> "ia:${IAUtils.getCustomStack(itemStack)?.namespacedID}"
            hasPDC(itemStack, "nexo", "id") -> "nexo:${NexoUtils.getID(itemStack)}"
            MythicUtils.isMythicItem(itemStack) -> "mm:${MythicUtils.getMythicItemID(itemStack)}"
            hasNBT(itemStack, "MMOITEMS_ITEM_TYPE") -> "mmo:${MMOUtils.getNamespaceID(itemStack)}"
            HDBUtils.isHDBItem(itemStack) -> "hdb:${HDBUtils.getID(itemStack)}"
            else -> itemStack.type.toString()
        }
    }

    fun getCustomItemName(itemStack: ItemStack): String {
        if (!hasNBT(itemStack, "itemsadder") && !MythicUtils.isMythicItem(itemStack) && !hasNBT(itemStack, "MMOITEMS_ITEM_TYPE")) {
            if (hasPDC(itemStack, "nexo", "id")) {
                val itemName = NexoUtils.getItemBuilder(itemStack)?.itemName
                if (itemName != null) return PlainTextComponentSerializer.plainText().serialize(itemName) else return "<lang:${itemStack.type.itemTranslationKey}>"
            }

            if (HDBUtils.isHDBItem(itemStack)) {
                val hdbItemName = getItemName(itemStack)
                return PlainTextComponentSerializer.plainText().serialize(hdbItemName)
            }

        } else {
            val itemName = getItemName(itemStack)
            return PlainTextComponentSerializer.plainText().serialize(itemName)
        }

        return "<lang:${itemStack.type.itemTranslationKey}>"
    }

    /*fun getItemName(itemStack: ItemStack): Component {
        return itemStack.itemMeta.itemName()
    }*/

    fun getItemName(itemStack: ItemStack): Component {
        val version = VersionUtils.version

        return when {
            version == null -> Component.text("Unsupported Version")
            VersionUtils.isVersion_1_21 ->  itemStack.itemMeta.itemName()
            VersionUtils.isVersion_1_20_5 -> itemStack.itemMeta.displayName()!!
            VersionUtils.isVersion_1_20 -> itemStack.itemMeta.displayName()!!
            VersionUtils.isVersion_1_19_4 -> itemStack.itemMeta.displayName()!!
            else -> Component.text("Unsupported Version")
        }
    }

    private fun hasNBT(itemStack: ItemStack?, key: String): Boolean {
        if (itemStack == null || itemStack.type == Material.AIR || !itemStack.hasItemMeta()) return false
        return try {
            val itemNbt: ReadableNBT = NBT.readNbt(itemStack)
            itemNbt.hasTag(key)
        } catch (e: Exception) {
            false
        }
    }

    private fun hasPDC(itemStack: ItemStack?, namespace: String, key: String): Boolean {
        if (itemStack == null || !itemStack.hasItemMeta()) return false
        val meta = itemStack.itemMeta
        val PDC = meta.persistentDataContainer
        val namespacedKey = getNamespacedKey(namespace, key)
        return PDC.has(namespacedKey)
    }

    private fun getMythicItem(itemName: String): ItemStack {
        val mythicItem: MythicItem = MythicUtils.getMythicItem(itemName)
        return BukkitAdapter.adapt(mythicItem.generateItemStack(1))
    }

    private fun getMMOItemsItem(itemName: String): ItemStack {
        val type = itemName.split(":")[0]
        val id = itemName.substring(itemName.indexOf(":") + 1)
        return MMOUtils.getMMOItemsItem(type, id)
    }

    private fun setItemMeta(itemStack: ItemStack?, name: Component?, lore: List<Component>?): ItemStack? {
        if (itemStack == null || itemStack.type == Material.AIR) return null

        val meta = itemStack.itemMeta
        name?.let { meta.displayName(it) }
        lore?.let { meta.lore(it) }
        itemStack.itemMeta = meta
        return itemStack
    }

    private fun getNamespacedKey(namespace: String, key: String): NamespacedKey = NamespacedKey(namespace, key)

    fun getCustomItem(itemName: String, name: String, lore: List<String>): ItemStack? {
        val itemStack = getCustomItem(itemName)
        return getCustomItem(itemStack, name, lore)
    }

    fun getCustomItem(itemStack: ItemStack, name: String, lore: List<String>): ItemStack? {
        val nameComponent = StringUtils.getComponent(name)
        val loreComponent = StringUtils.getComponentList(lore)
        return setItemMeta(itemStack, nameComponent, loreComponent)
    }

    fun getCustomItem(itemName: String, name: String, lore: String): ItemStack {
        val itemStack: ItemStack = getCustomItem(itemName, name)
        val meta = itemStack.itemMeta
        val strList = listOf(lore)
        val componentList = StringUtils.getComponentList(strList)
        meta.lore(componentList)
        itemStack.setItemMeta(meta)
        return itemStack
    }

    fun getCustomItem(itemName: String, amount: Int): ItemStack {
        return getCustomItem(itemName).apply { setAmount(amount) }
    }

    fun getCustomItem(itemName: String, name: String): ItemStack {
        val itemStack: ItemStack = getCustomItem(itemName)
        val meta = itemStack.itemMeta
        val componentName = StringUtils.getComponent(name)
        meta.displayName(componentName)
        itemStack.setItemMeta(meta)
        return itemStack
    }

    fun getCustomItem(itemStack: ItemStack, lore: List<String>): ItemStack? {
        val loreComponent = StringUtils.getComponentList(lore)
        return setItemMeta(itemStack, null, loreComponent)
    }

    fun isCustomItem(itemStack: ItemStack, customItem: ItemStack): Boolean {
        return itemStack.isSimilar(customItem)
    }
}
