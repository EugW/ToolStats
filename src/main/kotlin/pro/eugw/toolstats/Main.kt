package pro.eugw.toolstats

import com.google.gson.JsonParser
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

fun main() {
    Main()
}

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        super.onEnable()
        try {
            Class.forName("de.tr7zw.nbtapi.NBTCompound")
        } catch (e: Exception) {
            infoc("****************************************************")
            infoc("*  Failed to find NBT API plugin, please download  *")
            infoc("* https://www.spigotmc.org/resources/nbt-api.7939/ *")
            infoc("****************************************************")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }
        if (Utils.isServerOlderThan114())
            infoc("Server is older than 1.14 - Using old Lore identifiers")
        Bukkit.getPluginManager().registerEvents(this, this)
        saveDefaultConfig()
        Utils.initDataFolder(this)
        thread(true) {
            infoc("Version: ${description.version} enabled. Update available: ${description.version != try {
                JsonParser().parse((URL("https://api.github.com/repos/EugW/toolstats/releases/latest").openConnection() as HttpsURLConnection).inputStream.reader()).asJsonObject["tag_name"].asString
            } catch (e: Exception) {
                description.version
            }}")
        }
    }

    override fun onDisable() {
        super.onDisable()
        infoc("Disabled")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (command.name) {
            "toolstats" -> {
                if (sender.hasPermission("toolstats.reload")) {
                    reloadConfig()
                    info(config.getString("settings.reload")!!, sender)
                } else {
                    info(config.getString("settings.noPerm")!!, sender)
                }
                return true
            }
            "tstracking" -> {
                if (sender.hasPermission("toolstats.tstracking")) {
                    if (sender !is Player)
                        return true
                    if (args.size >= 2) {
                        if (arrayListOf("break", "killPlayer", "killMob").contains(args[0]) && arrayListOf("start", "stop").contains(args[1]))
                            UniversalLogic.setTrackingStatus(config, sender.player!!, args[0], when (args[1]) {
                                "start" -> true
                                "stop" -> false
                                else -> null!! //hack to disable anything else
                            })
                        else
                            info(config.getString("settings.noArg")!!, sender)
                    }
                    else
                        info(config.getString("settings.noArg")!!, sender)
                } else {
                    info(config.getString("settings.noPerm")!!, sender)
                }
                return true
            }
        }
        return super.onCommand(sender, command, label, args)
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (!config.getBoolean("break.enabled"))
            return
        val player = event.player
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("break.worlds"))
            return
        UniversalLogic.calculate(config, player, "break", server)
    }

    @EventHandler
    fun onKillPlayer(event: EntityDeathEvent) {
        if (event.entity !is Player)
            return
        if (!config.getBoolean("killPlayer.enabled"))
            return
        if (event.entity.killer == null || event.entity.killer !is Player)
            return
        val player = event.entity.killer!!
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("killPlayer.worlds"))
            return
        UniversalLogic.calculate(config, player, "killPlayer", server)
    }

    @EventHandler
    fun onKillMob(event: EntityDeathEvent) {
        if (event.entity is Player)
            return
        if (!config.getBoolean("killMob.enabled"))
            return
        if (event.entity.killer == null || event.entity.killer !is Player)
            return
        val player = event.entity.killer!!
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("killMob.worlds"))
            return
        UniversalLogic.calculate(config, player, "killMob", server)
    }

    private fun info(msg: String, sender: CommandSender) {
        sender.sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${msg.replace("&", "\u00a7")}")
    }

    private fun infoc(msg: String) {
        Bukkit.getConsoleSender().sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${msg.replace("&", "\u00a7")}")
    }

}
