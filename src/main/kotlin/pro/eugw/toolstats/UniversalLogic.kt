package pro.eugw.toolstats

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class UniversalLogic {
    
    private val dividerChar = "§&"

    fun calculate(config: FileConfiguration, player: Player, type: String, server: Server) {
        @Suppress("DEPRECATION") val itemStack = player.inventory.itemInHand
        if (itemStack.type.toString() !in config.getStringList("$type.tools"))
            return
        val itemMeta = itemStack.itemMeta!!
        if (!config.getBoolean("$type.trackItemsWCustomNames") && itemMeta.displayName.isNotBlank())
            return
        val lore = if (itemMeta.lore == null) ArrayList<String>() else itemMeta.lore!!
        var tracking = config.getBoolean("$type.autoStartTracking")
        with(lore.find { it.replace("§", "").startsWith("tStart") && it.replace("§", "").endsWith("tEnd") }) {
            if (this != null)
                tracking = this.replace("§", "")[6] == '1'
        }
        if (!tracking)
            return
        val lts = mapOf(
                Pair("killPlayer", "kp"),
                Pair("killMob", "km"),
                Pair("break", "br")
        )
        var index: Int
        with(lore.find { it.replace("§", "").startsWith("tStart")
                && it.replace("§", "").endsWith("tEnd")
                && (it.replace("§", "").substring(7, 9) == lts[type]
                || it.replace("§", "").substring(7, 9) == "an") }) {
            index = if (this == null) {
                val trackingBool = if (tracking) "1" else "0"
                val preparedString = makeHidden("tStart") +
                        makeHidden(trackingBool) +
                        makeHidden(lts.getValue(type)) +
                        config.getStringList("$type.lore")[0]!!.replace("&", "\u00a7").replace("%count%", "0") +
                        dividerChar +
                        makeHidden(0) +
                        dividerChar +
                        makeHidden("tEnd")
                lore.add(preparedString)
                lore.indexOf(preparedString)
            } else
                lore.indexOf(this)
        }
        var count = lore[index].split(dividerChar)[1].replace("§", "").toInt()
        count++
        for (i in 1..config.getString("$type.reward.count")!!.split(",").size) {
            if (config.getBoolean("$type.reward.enabled") && count == config.getString("$type.reward.count")!!.split(",")[i - 1].toInt() && player.hasPermission("toolstats.upgrade")) {
                if (config.getBoolean("$type.reward.enchant$i.enabled")) {
                    for (pre in config.getStringList("$type.reward.enchant$i.ench")) {
                        val ench = pre.split(":")
                        @Suppress("DEPRECATION") val enchantment = Enchantment.getByName(ench[0])!!
                        if (ench[1].toInt() > itemMeta.getEnchantLevel(enchantment))
                            if (config.getBoolean("$type.reward.enchant$i.applyOnlyCompatible")) {
                                if (enchantment.canEnchantItem(itemStack))
                                    itemMeta.addEnchant(enchantment, ench[1].toInt(), true)
                            } else {
                                itemMeta.addEnchant(enchantment, ench[1].toInt(), true)
                            }
                    }
                }
                if (config.getBoolean("$type.reward.exp$i.enabled")) {
                    player.giveExp(config.getInt("$type.reward.exp$i.lvl"))
                }
                if (config.getBoolean("$type.reward.items$i.enabled")) {
                    for (pre in config.getStringList("$type.reward.items$i.give")) {
                        val item = pre.split(":")
                        player.inventory.addItem(ItemStack(Material.getMaterial(item[0])!!, item[1].toInt()))
                    }
                }
                if (config.getBoolean("$type.reward.sound$i.enabled")) {
                    player.playSound(player.location, Sound.valueOf(config.getString("$type.reward.sound$i.sound")!!), 1f, 1f)
                }
                if (config.getBoolean("$type.reward.command$i.enabled")) {
                    Bukkit.dispatchCommand(server.consoleSender, config.getString("$type.reward.command$i.command")!!.replace("%nickname%", player.name))
                }
            }
        }
        val trackingBool = if (tracking) "1" else "0"
        val preparedString = makeHidden("tStart") +
                makeHidden(trackingBool) +
                makeHidden(lts.getValue(type)) +
                config.getStringList("$type.lore")[0]!!.replace("&", "\u00a7").replace("%count%", "$count") +
                dividerChar +
                makeHidden(count) +
                dividerChar +
                makeHidden("tEnd")
        lore[index] = preparedString
        val arr = config.getStringList("$type.lore").drop(1)
        arr.forEach {
            if (lore.size == index + arr.indexOf(it) + 1)
                lore.add("")
            lore[index + arr.indexOf(it) + 1] = it.replace("&", "\u00a7").replace("%count%", "$count")
        }
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
        @Suppress("DEPRECATION") player.inventory.setItemInHand(itemStack)
    }

    fun setTrackingStatus(config: FileConfiguration, player: Player, status: Boolean) {
        @Suppress("DEPRECATION") val itemStack = player.inventory.itemInHand
        if (itemStack.type.toString() !in config.getStringList("killPlayer.tools") && itemStack.type.toString() !in config.getStringList("killMob.tools") && itemStack.type.toString() !in config.getStringList("break.tools"))
            return
        val itemMeta = itemStack.itemMeta!!
        val lore = if (itemMeta.lore == null) ArrayList<String>() else itemMeta.lore!!
        val trackingBool = if (status) '1' else '0'
        val preparedString = makeHidden("tStart") +
                makeHidden(trackingBool) +
                makeHidden("an") +
                dividerChar +
                makeHidden(0) +
                dividerChar +
                makeHidden("tEnd")
        val matchingIndexes = ArrayList<Int>()
        for (i in 0 until lore.size) {
            if (lore[i].replace("§", "").startsWith("tStart") && lore[i].replace("§", "").endsWith("tEnd"))
                matchingIndexes.add(i)
        }
        if (matchingIndexes.size == 0)
            lore.add(preparedString)
        else {
            matchingIndexes.forEach {
                lore[it] = lore[it].replaceIndex(13, trackingBool)
            }
        }
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
        @Suppress("DEPRECATION") player.inventory.setItemInHand(itemStack)
        player.sendMessage("${config.getString("settings.prefix")!!.replace("&", "\u00a7")} ${config.getString("settings.trackSwitch")!!.replace("&", "\u00a7")}")
    }

    private fun makeHidden(origin: Any): String {
        val o = origin.toString()
        var result = ""
        o.forEach {
            result += "§$it"
        }
        return result
    }

    private fun String.replaceIndex(index: Int, replacement: Char): String {
        var new = ""
        for (i in 0 until length) {
            new += if (i == index)
                replacement
            else
                this[i]
        }
        return new
    }

}