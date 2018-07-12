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
import pro.eugw.toolstats.nms.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    Main()
}

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        super.onEnable()
        Bukkit.getPluginManager().registerEvents(this, this)
        saveDefaultConfig()
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

    override fun onCommand(sender: CommandSender, command: Command, label: String?, args: Array<String>): Boolean {
        when (command.name) {
            "toolstats" -> {
                if (sender.hasPermission("toolstats.reload")) {
                    reloadConfig()
                    info(config.getString("settings.reload"), sender)
                } else {
                    info(config.getString("settings.noPerm"), sender)
                }
                return true
            }
            "tstracking" -> {
                if (sender.hasPermission("toolstats.tstracking")) {
                    if (sender !is Player)
                        return true
                    if (args.isNotEmpty())
                        when (args[0]) {
                            "start" -> toggleTracking(sender.player, true)
                            "stop" -> toggleTracking(sender.player, false)
                        }
                } else {
                    info(config.getString("settings.noPerm"), sender)
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
        nmsInvoke(player, "break")
    }

    @EventHandler
    fun onKillPlayer(event: EntityDeathEvent) {
        if (event.entity !is Player)
            return
        if (!config.getBoolean("killPlayer.enabled"))
            return
        if (event.entity.killer == null || event.entity.killer !is Player)
            return
        val player = event.entity.killer
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("killPlayer.worlds"))
            return
        nmsInvoke(player, "killPlayer")
    }

    @EventHandler
    fun onKillMob(event: EntityDeathEvent) {
        if (event.entity is Player)
            return
        if (!config.getBoolean("killMob.enabled"))
            return
        if (event.entity.killer == null || event.entity.killer !is Player)
            return
        val player = event.entity.killer
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("killMob.worlds"))
            return
        nmsInvoke(player, "killMob")
    }

    private fun nmsInvoke(player: Player, type: String) {
        when (Bukkit.getBukkitVersion()) {
            //"1.7.2-R0.4-SNAPSHOT" -> MC17R1(config, player, type, server).calculate()
            //"1.7.5-R0.1-SNAPSHOT" -> MC17R2(config, player, type, server).calculate()
            //"1.7.8-R0.1-SNAPSHOT", "1.7.9-R0.2-SNAPSHOT" -> MC17R3(config, player, type, server).calculate()
            //"1.7.10-R0.1-SNAPSHOT" -> MC17R4(config, player, type, server).calculate()
            "1.8-R0.1-SNAPSHOT" -> MC18R1(config, player, type, server).calculate()
            "1.8.3-R0.1-SNAPSHOT" -> MC18R2(config, player, type, server).calculate()
            "1.8.8-R0.1-SNAPSHOT" -> MC18R3(config, player, type, server).calculate()
            "1.9-R0.1-SNAPSHOT", "1.9.2-R0.1-SNAPSHOT" -> MC19R1(config, player, type, server).calculate()
            "1.9.4-R0.1-SNAPSHOT" -> MC19R2(config, player, type, server).calculate()
            "1.10.2-R0.1-SNAPSHOT" -> MC110R1(config, player, type, server).calculate()
            "1.11-R0.1-SNAPSHOT", "1.11.2-R0.1-SNAPSHOT" -> MC111R1(config, player, type, server).calculate()
            "1.12-R0.1-SNAPSHOT", "1.12.1-R0.1-SNAPSHOT", "1.12.2-R0.1-SNAPSHOT" -> MC112R1(config, player, type, server).calculate()
            else -> infoc("Oops, non-supported version of the server. If it seems to you that this error please report to the topic on SpigotMC")
        }
    }

    private fun toggleTracking(player: Player, status: Boolean) {
        when (Bukkit.getBukkitVersion()) {
            "1.8-R0.1-SNAPSHOT" -> MC18R1(config, player, "", server).setTrackingStatus(status)
            "1.8.3-R0.1-SNAPSHOT" -> MC18R2(config, player, "", server).setTrackingStatus(status)
            "1.8.8-R0.1-SNAPSHOT" -> MC18R3(config, player, "", server).setTrackingStatus(status)
            "1.9-R0.1-SNAPSHOT", "1.9.2-R0.1-SNAPSHOT" -> MC19R1(config, player, "", server).setTrackingStatus(status)
            "1.9.4-R0.1-SNAPSHOT" -> MC19R2(config, player, "", server).setTrackingStatus(status)
            "1.10.2-R0.1-SNAPSHOT" -> MC110R1(config, player, "", server).setTrackingStatus(status)
            "1.11-R0.1-SNAPSHOT", "1.11.2-R0.1-SNAPSHOT" -> MC111R1(config, player, "", server).setTrackingStatus(status)
            "1.12-R0.1-SNAPSHOT", "1.12.1-R0.1-SNAPSHOT", "1.12.2-R0.1-SNAPSHOT" -> MC112R1(config, player, "", server).setTrackingStatus(status)
            else -> {
                infoc("Oops, non-supported version of the server. If it seems to you that this error please report to the topic on SpigotMC")
                return
            }
        }
        info(config.getString("settings.trackSwitch"), player)
    }

    private fun info(msg: String, sender: CommandSender) {
        sender.sendMessage("\u00a77[\u00a7eToolStats\u00a77] $msg")
    }

    private fun infoc(msg: String) {
        Bukkit.getConsoleSender().sendMessage("\u00a77[\u00a7eToolStats\u00a77] $msg")
    }

}
