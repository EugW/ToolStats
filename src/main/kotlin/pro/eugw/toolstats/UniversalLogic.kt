package pro.eugw.toolstats

import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

@Suppress("DEPRECATION")
class UniversalLogic(private val config: FileConfiguration, private val player: Player, private val type: String, private val server: Server) {

    private val dividerChar = "§&"

    fun calculate() {
        val itemStack = player.inventory.itemInHand
        if (itemStack.type.toString() !in config.getStringList("$type.tools"))
            return
        val itemMeta = itemStack.itemMeta
        val lore = if (itemMeta.lore == null) ArrayList<String>() else itemMeta.lore
        var tracking = config.getBoolean("$type.autoStartTracking")
        with(lore.find { it.replace("§", "").startsWith("tStart") && it.replace("§", "").endsWith("tEnd") }) {
            if (this != null) {
                tracking = this.replace("§", "")[6] == '1'
            }
        }
        if (!tracking)
            return
        val lts = mapOf(
                Pair("killPlayer", "kp"),
                Pair("killMob", "km"),
                Pair("break", "br")
        )
        var index = 0
        with(lore.find {
            it.replace("§", "").startsWith("tStart")
                    && it.replace("§", "").endsWith("tEnd")
                    && (it.replace("§", "").substring(7, 9) == lts[type] || it.replace("§", "").substring(7, 9) == "an") }) {
            index = if (this == null) {
                val trackingBool = if (tracking) "1" else "0"
                val preparedString = makeHidden("tStart") +
                        makeHidden(trackingBool) +
                        makeHidden(lts[type]!!) +
                        config.getString("$type.lore").replace("&", "\u00a7").replace("%count%", "0") +
                        dividerChar +
                        makeHidden(0) +
                        dividerChar +
                        makeHidden("tEnd")
                lore.add(preparedString)
                lore.indexOf(preparedString)
            } else {
                lore.indexOf(this)
            }
        }
        var count = lore[index].split(dividerChar)[1].replace("§", "").toInt()
        count++
        for (i in 1..config.getString("$type.reward.count").split(",").size) {
            if (config.getBoolean("$type.reward.enabled") && count == config.getString("$type.reward.count").split(",")[i - 1].toInt() && player.hasPermission("toolstats.upgrade")) {
                if (config.getBoolean("$type.reward.enchant$i.enabled")) {
                    for (pre in config.getStringList("$type.reward.enchant$i.ench")) {
                        val ench = pre.split(":")
                        if (ench[1].toInt() > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0])))
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), ench[1].toInt(), true)
                    }
                }
                if (config.getBoolean("$type.reward.exp$i.enabled")) {
                    player.giveExp(config.getInt("$type.reward.exp$i.lvl"))
                }
                if (config.getBoolean("$type.reward.items$i.enabled")) {
                    for (pre in config.getStringList("$type.reward.items$i.give")) {
                        val item = pre.split(":")
                        player.inventory.addItem(org.bukkit.inventory.ItemStack(Material.getMaterial(item[0]), item[1].toInt()))
                    }
                }
                if (config.getBoolean("$type.reward.sound$i.enabled")) {
                    player.playSound(player.location, Sound.valueOf(config.getString("$type.reward.sound$i.sound")), 1f, 1f)
                }
                if (config.getBoolean("$type.reward.command$i.enabled")) {
                    Bukkit.dispatchCommand(server.consoleSender, config.getString("$type.reward.command$i.command").replace("%nickname%", player.name))
                }
            }
        }
        val trackingBool = if (tracking) "1" else "0"
        val preparedString = makeHidden("tStart") +
                makeHidden(trackingBool) +
                makeHidden(lts[type]!!) +
                config.getString("$type.lore").replace("&", "\u00a7").replace("%count%", "$count") +
                dividerChar +
                makeHidden(count) +
                dividerChar +
                makeHidden("tEnd")
        lore[index] = preparedString
        itemMeta.lore = lore
        itemStack.itemMeta = itemMeta
        player.inventory.itemInHand = itemStack
    }

    fun setTrackingStatus(status: Boolean) {
        val itemStack = player.inventory.itemInHand
        val itemMeta = itemStack.itemMeta
        val lore = if (itemMeta.lore == null) ArrayList<String>() else itemMeta.lore
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
        player.inventory.itemInHand = itemStack
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