package pro.eugw.toolstats

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.Exception

object UniversalLogic {

    fun calculate(config: FileConfiguration, player: Player, type: String, server: Server) {
        @Suppress("DEPRECATION") val itemStack = player.inventory.itemInHand
        if (itemStack.type.toString() !in config.getStringList("$type.tools"))
            return
        val nbtItem = NBTItem(itemStack)
        if (!config.getBoolean("$type.trackItemsWCustomNames")/* && itemMeta.displayName.isNotBlank()*/)
            return
        if (!nbtItem.hasKey("display"))
            nbtItem.addCompound("display")
        println(nbtItem.compound)
        val lore = nbtItem.getCompound("display").getStringList("Lore")
        val trackingNBTTag = "ToolStats.$type.tracking"
        val tracking = if (nbtItem.hasKey(trackingNBTTag)) nbtItem.getBoolean(trackingNBTTag) else {
            nbtItem.setBoolean(trackingNBTTag, config.getBoolean("$type.autoStartTracking"))
            config.getBoolean("$type.autoStartTracking")
        }
        if (!tracking)
            return
        val countNBTTag = "ToolStats.$type.count"
        var count = if (nbtItem.hasKey(countNBTTag)) nbtItem.getInteger(countNBTTag) else {
            nbtItem.setInteger(countNBTTag, 0)
            0
        }
        count++
        nbtItem.setInteger(countNBTTag, count)
//        for (i in 1..config.getString("$type.reward.count")!!.split(",").size) {
//            if (config.getBoolean("$type.reward.enabled") && count == config.getString("$type.reward.count")!!.split(",")[i - 1].toInt() && player.hasPermission("toolstats.upgrade")) {
//                if (config.getBoolean("$type.reward.enchant$i.enabled")) {
//                    for (pre in config.getStringList("$type.reward.enchant$i.ench")) {
//                        val ench = pre.split(":")
//                        @Suppress("DEPRECATION") val enchantment = Enchantment.getByName(ench[0])!!
//                        if (ench[1].toInt() > itemMeta.getEnchantLevel(enchantment))
//                            if (config.getBoolean("$type.reward.enchant$i.applyOnlyCompatible")) {
//                                if (enchantment.canEnchantItem(itemStack))
//                                    itemMeta.addEnchant(enchantment, ench[1].toInt(), true)
//                            } else {
//                                itemMeta.addEnchant(enchantment, ench[1].toInt(), true)
//                            }
//                    }
//                }
//                if (config.getBoolean("$type.reward.exp$i.enabled")) {
//                    player.giveExp(config.getInt("$type.reward.exp$i.lvl"))
//                }
//                if (config.getBoolean("$type.reward.items$i.enabled")) {
//                    for (pre in config.getStringList("$type.reward.items$i.give")) {
//                        val item = pre.split(":")
//                        player.inventory.addItem(ItemStack(Material.getMaterial(item[0])!!, item[1].toInt()))
//                    }
//                }
//                if (config.getBoolean("$type.reward.sound$i.enabled")) {
//                    player.playSound(player.location, Sound.valueOf(config.getString("$type.reward.sound$i.sound")!!), 1f, 1f)
//                }
//                if (config.getBoolean("$type.reward.command$i.enabled")) {
//                    Bukkit.dispatchCommand(server.consoleSender, config.getString("$type.reward.command$i.command")!!.replace("%nickname%", player.name))
//                }
//            }
//        }
        val cachedName = File(Utils.dataFolder(), type)
        if (!cachedName.exists())
            cachedName.writeText(config.getStringList("$type.lore")[0])
        var oldName: String? = null
        if (cachedName.readText() != config.getStringList("$type.lore")[0])
            oldName = cachedName.readText()
        cachedName.writeText(config.getStringList("$type.lore")[0])
        val result = lore.find {
            var baseTargetString = it
            if (Utils.isServerNewerThan113()) {
                baseTargetString = baseTargetString.replace("{\"text\":\"", "").replace("\"}", "")
            }
            val targetString = Regex("§[A-z0-9]").replace(baseTargetString, "")
            val configLore = Regex("&[A-z0-9]").replace(
                    oldName ?: config.getStringList("$type.lore")[0], "")
            if (configLore.contains("%count%"))
                targetString.startsWith(configLore.split("%count%")[0]) && targetString.endsWith(configLore.split("%count%")[1])
            else
                targetString == configLore
        }
        val index = if (result == null) lore.size else lore.indexOf(result)
        config.getStringList("$type.lore").forEachIndexed { offset, s ->
            var finalString = s.replace("&", "\u00a7").replace("%count%", "$count")
            if (Utils.isServerNewerThan113())
                finalString = "{\"text\":\"$finalString\"}"
            if (lore.size <= index + offset) {
                lore.add("")
            }
            lore[index + offset] = finalString
        }
        @Suppress("DEPRECATION") player.inventory.setItemInHand(nbtItem.item)
    }

    fun setTrackingStatus(config: FileConfiguration, player: Player, type: String, status: Boolean) {
        @Suppress("DEPRECATION") val itemStack = player.inventory.itemInHand
        if (itemStack.type.toString() !in config.getStringList("killPlayer.tools") && itemStack.type.toString() !in config.getStringList("killMob.tools") && itemStack.type.toString() !in config.getStringList("break.tools"))
            return
        val nbtItem = NBTItem(itemStack)
        nbtItem.setBoolean("ToolStats.$type.tracking", status)
        @Suppress("DEPRECATION") player.inventory.setItemInHand(nbtItem.item)
        player.sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${config.getString("settings.trackSwitch")!!.replace("&", "\u00a7")}")
    }

}