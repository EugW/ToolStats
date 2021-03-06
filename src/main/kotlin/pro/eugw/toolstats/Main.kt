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

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        super.onEnable()
        if (Utils.isServerNewerThan17())
            try {
                Class.forName("de.tr7zw.nbtapi.NBTCompound")
            } catch (e: Exception) {
                infoc("§4****************************************************")
                infoc("§4*§f  Failed to find NBT API plugin, please download  §4*")
                infoc("§4*§f https://www.spigotmc.org/resources/nbt-api.7939/ §4*")
                infoc("§4****************************************************")
                Bukkit.getPluginManager().disablePlugin(this)
                return
            }
        if (!Utils.isServerNewerThan113() && Utils.isServerNewerThan17())
            infoc("Server is older than 1.14 - Using old Lore identifiers")
        if (!Utils.isServerNewerThan17())
            infoc("Server is older than 1.8 - Using old Logic")
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
                if (sender is Player && sender.hasPermission("toolstats.tstracking")) {
                    if (Utils.isServerNewerThan17()) {
                        if (args.size >= 2) {
                            if (arrayListOf("break", "killPlayer", "killMob").contains(args[0]) && arrayListOf("start", "stop").contains(args[1]))
                                NBTLogic.setTrackingStatus(config, sender.player!!, args[0], when (args[1]) {
                                    "start" -> true
                                    "stop" -> false
                                    else -> null!! //hack to disable anything else
                                })
                            else
                                info(config.getString("settings.noArg")!!, sender)
                        } else
                            info(config.getString("settings.noArg")!!, sender)
                    } else {
                        if (args.isNotEmpty())
                            if (arrayListOf("start", "stop").contains(args[0]))
                                ClassicLogic.setTrackingStatus(config, sender.player!!, when (args[0]) {
                                    "start" -> true
                                    "stop" -> false
                                    else -> null!! //hack to disable anything else
                                })
                            else
                                info(config.getString("settings.noArg")!!, sender)
                    }
                } else {
                    info(config.getString("settings.noPerm")!!, sender)
                }
                return true
            }
            "tsclearlore" -> {
                if (sender is Player && sender.hasPermission("toolstats.tsclearlore")) {
                    if (Utils.isServerNewerThan17()) {
                        NBTLogic.resetLore(sender.player!!, config)
                    } else {
                        info(config.getString("settings.outdatedServer")!!, sender)
                    }
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
        if (Utils.isServerNewerThan17())
            NBTLogic.calculate(config, player, "break", server)
        else
            ClassicLogic.calculate(config, player, "break", server)
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
        if (Utils.isServerNewerThan17())
            NBTLogic.calculate(config, player, "killPlayer", server)
        else
            ClassicLogic.calculate(config, player, "killPlayer", server)
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
        if (Utils.isServerNewerThan17())
            NBTLogic.calculate(config, player, "killMob", server)
        else
            ClassicLogic.calculate(config, player, "killMob", server)
    }

    private fun info(msg: String, sender: CommandSender) {
        sender.sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${msg.replace("&", "\u00a7")}")
    }

    private fun infoc(msg: String) {
        Bukkit.getConsoleSender().sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${msg.replace("&", "\u00a7")}")
    }

}
