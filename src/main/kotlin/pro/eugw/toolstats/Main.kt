package pro.eugw.toolstats

import com.google.gson.JsonParser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        super.onEnable()
        Bukkit.getPluginManager().registerEvents(this, this)
        saveDefaultConfig()
        thread(true) { infoc("Version: ${description.version} enabled. Update available: ${description.version != try { JsonParser().parse((URL("https://api.github.com/repos/EugW/toolstats/releases/latest").openConnection() as HttpsURLConnection).inputStream.reader()).asJsonObject["tag_name"].asString } catch (e: Exception) { description.version } }") }
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
            /*"tsgivereward" -> {
                if (sender.hasPermission("toolstats.givereward")) {
                    if (!server.getPlayerExact(args[0]).isOnline) {
                        info(config.getString("settings.noPlayer"), sender)
                        return true
                    }
                    when (args[1]) {
                        "killPlayer" -> {

                        }
                        "killMob" -> {

                        }
                        "break" -> {

                        }
                        else -> {
                            info(config.getString("settings.noType"), sender)
                            return true
                        }
                    }
                }
            }*/
        }
        return super.onCommand(sender, command, label, args)
    }

    @EventHandler
    internal fun onBreak(event: BlockBreakEvent) {
        if (!config.getBoolean("break.enabled"))
            return
        val player = event.player
        if (!player.hasPermission("toolstats.counter"))
            return
        if (player.world.name !in config.getStringList("break.worlds"))
            return
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.toString() !in config.getStringList("break.tools"))
            return
        val itemMeta = itemStack.itemMeta
        val lore = ArrayList<String>()
        if (itemMeta.lore == null) {
            lore.add(config.getString("break.lore").replace("&", "\u00a7") + ": 0")
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        } else if (!itemMeta.lore.toString().contains(config.getString("break.lore").replace("&", "\u00a7"))) {
            lore.addAll(itemMeta.lore)
            lore.add(config.getString("break.lore").replace("&", "\u00a7") + ": 0")
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        }
        lore.addAll(itemMeta.lore)
        for (pre in lore) {
            if (pre.contains(config.getString("break.lore").replace("&", "\u00a7"))) {
                var cnt = Integer.valueOf(pre.replace("[\\D]".toRegex(), ""))
                cnt++
                b(cnt, itemMeta, player, config)
                lore[lore.indexOf(pre)] = config.getString("break.lore").replace("&", "\u00a7") + ": " + cnt
            }
        }
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
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
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.toString() !in config.getList("killPlayer.tools"))
            return
        val itemMeta = itemStack.itemMeta
        val lore = ArrayList<String>()
        if (itemMeta.lore == null) {
            if (config.getBoolean("killPlayer.enabled")) {
                lore.add(config.getString("killPlayer.lore").replace("&", "\u00a7") + ": 0")
            }
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        } else if (!itemMeta.lore.toString().contains(config.getString("killPlayer.lore").replace("&", "\u00a7"))) {
            lore.addAll(itemMeta.lore)
            if (config.getBoolean("killPlayer.enabled")) {
                lore.add(config.getString("killPlayer.lore").replace("&", "\u00a7") + ": 0")
            }
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        }
        lore.addAll(itemMeta.lore)
        for (pre in lore) {
            if (pre.contains(config.getString("killPlayer.lore").replace("&", "\u00a7"))) {
                var cnt = Integer.valueOf(pre.replace("[\\D]".toRegex(), ""))
                cnt++
                kp(cnt, itemMeta, player, config)
                lore[lore.indexOf(pre)] = config.getString("killPlayer.lore").replace("&", "\u00a7") + ": " + cnt
            }
        }
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
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
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.toString() !in config.getList("killMob.tools"))
            return
        val itemMeta = itemStack.itemMeta
        val lore = ArrayList<String>()
        if (itemMeta.lore == null) {
            if (config.getBoolean("killMob.enabled")) {
                lore.add(config.getString("killMob.lore").replace("&", "\u00a7") + ": 0")
            }
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        } else if (!itemMeta.lore.toString().contains(config.getString("killMob.lore").replace("&", "\u00a7"))) {
            lore.addAll(itemMeta.lore)
            if (config.getBoolean("killMob.enabled")) {
                lore.add(config.getString("killMob.lore").replace("&", "\u00a7") + ": 0")
            }
            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta
            lore.clear()
        }
        lore.addAll(itemMeta.lore)
        for (pre in lore) {
            if (pre.contains(config.getString("killMob.lore").replace("&", "\u00a7"))) {
                var cnt = Integer.valueOf(pre.replace("[\\D]".toRegex(), ""))
                cnt++
                km(cnt, itemMeta, player, config)
                lore[lore.indexOf(pre)] = config.getString("killMob.lore").replace("&", "\u00a7") + ": " + cnt
            }
        }
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
    }

    private fun b(count: Int?, itemMeta: ItemMeta, player: Player, fileConfiguration: FileConfiguration) {
        for (i in 1..fileConfiguration.getString("break.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size) {
            if (fileConfiguration.getBoolean("break.reward.enabled") && count == Integer.valueOf(fileConfiguration.getString("break.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[i - 1]) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("break.reward.enchant$i.enabled")) {
                    for (pre in fileConfiguration.getList("break.reward.enchant$i.ench")) {
                        val ench = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true)
                        }
                    }
                    if (fileConfiguration.getBoolean("break.reward.exp$i.enabled")) {
                        player.giveExp(fileConfiguration.getInt("break.reward.exp$i.lvl"))
                    }
                    if (fileConfiguration.getBoolean("break.reward.items$i.enabled")) {
                        for (pre in fileConfiguration.getList("break.reward.items$i.give")) {
                            val item = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            player.inventory.addItem(ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])))
                        }
                    }
                    if (fileConfiguration.getBoolean("break.reward.sound$i.enabled")) {
                        player.playSound(player.location, Sound.valueOf(fileConfiguration.getString("break.reward.sound$i.sound")), 1f, 1f)
                    }
                }
            }
        }
    }

    private fun kp(count: Int?, itemMeta: ItemMeta, player: Player, fileConfiguration: FileConfiguration) {
        for (i in 1..fileConfiguration.getString("killPlayer.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size) {
            if (fileConfiguration.getBoolean("killPlayer.reward.enabled") && count == Integer.valueOf(fileConfiguration.getString("killPlayer.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[i - 1]) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("killPlayer.reward.enchant$i.enabled")) {
                    for (pre in fileConfiguration.getList("killPlayer.reward.enchant$i.ench")) {
                        val ench = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true)
                        }
                    }
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.exp$i.enabled")) {
                    player.giveExp(fileConfiguration.getInt("killPlayer.reward.exp$i.lvl"))
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.items$i.enabled")) {
                    for (pre in fileConfiguration.getList("killPlayer.reward.items$i.give")) {
                        val item = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        player.inventory.addItem(ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])))
                    }
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.sound$i.enabled")) {
                    player.playSound(player.location, Sound.valueOf(fileConfiguration.getString("killPlayer.reward.sound$i.sound")), 1f, 1f)
                }

            }
        }

    }

    private fun km(count: Int?, itemMeta: ItemMeta, player: Player, fileConfiguration: FileConfiguration) {
        for (i in 1..fileConfiguration.getString("killMob.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size) {
            if (fileConfiguration.getBoolean("killMob.reward.enabled") && count == Integer.valueOf(fileConfiguration.getString("killMob.reward.count").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[i - 1]) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("killMob.reward.enchant$i.enabled")) {
                    for (pre in fileConfiguration.getList("killMob.reward.enchant$i.ench")) {
                        val ench = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true)
                        }
                    }
                }
                if (fileConfiguration.getBoolean("killMob.reward.exp$i.enabled")) {
                    player.giveExp(fileConfiguration.getInt("killMob.reward.exp$i.lvl"))
                }
                if (fileConfiguration.getBoolean("killMob.reward.items$i.enabled")) {
                    for (pre in fileConfiguration.getList("killMob.reward.items$i.give")) {
                        val item = pre.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        player.inventory.addItem(ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])))
                    }
                }
                if (fileConfiguration.getBoolean("killMob.reward.sound$i.enabled")) {
                    player.playSound(player.location, Sound.valueOf(fileConfiguration.getString("killMob.reward.sound$i.sound")), 1f, 1f)
                }
            }
        }
    }

    private fun info(msg: String, sender: CommandSender) {
        sender.sendMessage("\u00a77[\u00a7eToolStats\u00a77] $msg")
    }

    private fun infoc(msg: String) {
        Bukkit.getConsoleSender().sendMessage("\u00a77[\u00a7eToolStats\u00a77] $msg")
    }

}