package pro.eugw.toolstats

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Utils {

    private var majorVersion: Int? = null
    private var minorVersion: Int? = null
    private var pluginPath: File? = null

    private fun detectVersion() {
        val versionArray = Bukkit.getServer()::class.java.getPackage().name.split(".")[3].replace("v", "").split("_")
        majorVersion = versionArray[0].toInt()
        minorVersion = versionArray[1].toInt()
    }

    private fun isServerNewer(comparableVersion: String): Boolean {
        if (majorVersion == null || minorVersion == null)
            detectVersion()
        val versionArray = comparableVersion.split(".")
        if (majorVersion!! > versionArray[0].toInt()) {
            return true
        } else if (minorVersion!! > versionArray[1].toInt()) {
            return true
        }
        return false
    }

    fun isServerNewerThan113(): Boolean = isServerNewer("1.13")

    fun isServerNewerThan17(): Boolean = isServerNewer("1.7")

    fun initDataFolder(plugin: JavaPlugin) {
        val file = File(plugin.dataFolder, "cached")
        if (!file.exists())
            file.mkdir()
        pluginPath = file
    }

    fun dataFolder(): File {
        return pluginPath!!
    }

}