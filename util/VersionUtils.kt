// package 

import org.bukkit.Bukkit
import java.util.regex.Pattern

object VersionUtils {
    private val VERSION_PATTERN: Pattern = Pattern.compile("1\\.(19|20|21)(\\.\\d+)?")

    val version: String?
        get() = getBukkitVersion(Bukkit.getBukkitVersion())

    fun getBukkitVersion(version: String): String? {
        val matcher = VERSION_PATTERN.matcher(version)
        return if (matcher.find()) matcher.group() else null
    }

    val isVersion_1_21: Boolean
        get() {
            val version = version
            return version != null && version.matches("1\\.(21|21(\\.\\d+)?)".toRegex())
        }

    val isVersion_1_20_5: Boolean
        get() {
            val version = version
            return version != null && version.matches("1\\.(20\\.5|20\\.6(\\.\\d+)?)".toRegex())
        }

    val isVersion_1_20: Boolean
        get() {
            val version = version
            return version != null && version.matches("1\\.(19\\.4|20(\\.\\d+)?)".toRegex())
        }

    val isVersion_1_19_4: Boolean
        get() {
            val version = version
            return "1.19.4" == version
        }
}