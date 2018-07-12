package pro.eugw.toolstats.nms

import net.minecraft.server.v1_7_R3.NBTBase
import net.minecraft.server.v1_7_R3.NBTTagCompound
import net.minecraft.server.v1_7_R3.NBTTagList
import net.minecraft.server.v1_7_R3.NBTTagString
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

@Suppress("DEPRECATION")
class MC17R3(private val config: FileConfiguration, private val player: Player, private val type: String, private val server: Server) {

    fun calculate() {
        val enchantments = mapOf(
                Pair("PROTECTION_ENVIRONMENTAL", 0),
                Pair("PROTECTION_FIRE", 1),
                Pair("PROTECTION_FALL", 2),
                Pair("PROTECTION_EXPLOSIONS", 3),
                Pair("PROTECTION_PROJECTILE", 4),
                Pair("OXYGEN", 5),
                Pair("WATER_WORKER", 6),
                Pair("THORNS", 7),
                Pair("DEPTH_STRIDER", 8),
                Pair("FROST_WALKER", 9),
                Pair("BINDING_CURSE", 10),
                Pair("DAMAGE_ALL", 16),
                Pair("DAMAGE_UNDEAD", 17),
                Pair("DAMAGE_ARTHROPODS", 18),
                Pair("KNOCKBACK", 19),
                Pair("FIRE_ASPECT", 20),
                Pair("LOOT_BONUS_MOBS", 21),
                Pair("SWEEPING_EDGE", 22),
                Pair("DIG_SPEED", 32),
                Pair("SILK_TOUCH", 33),
                Pair("DURABILITY", 34),
                Pair("LOOT_BONUS_BLOCKS", 35),
                Pair("ARROW_DAMAGE", 48),
                Pair("ARROW_KNOCKBACK", 49),
                Pair("ARROW_FIRE", 50),
                Pair("ARROW_INFINITE", 51),
                Pair("LUCK", 61),
                Pair("LURE", 62),
                Pair("MENDING", 70),
                Pair("VANISHING_CURSE", 71)
        )
        val itemStackOrigin = player.inventory.itemInHand
        if (itemStackOrigin.type.toString() !in config.getStringList("$type.tools"))
            return
        val itemStackNMS = CraftItemStack.asNMSCopy(itemStackOrigin)
        if (itemStackNMS.tag == null)
            itemStackNMS.tag = NBTTagCompound()
        println(itemStackNMS.tag.toString())
        val tracking = if (itemStackNMS.tag!!.hasKey("pro.eugw.toolstats.tracking")) itemStackNMS.tag!!.getBoolean("pro.eugw.toolstats.tracking") else config.getBoolean("$type.autoStartTracking")
        if (!tracking)
            return
        var lore = itemStackNMS.tag!!.getCompound("display").getList("Lore", 8)
        var string = ""
        config.getString("$type.lore").replace("&", "\u00a7").split("%count%").dropLastWhile { it.isEmpty() }.forEach {
            string += "\\Q$it\\E.*\\d+.*"
        }
        string.replaceAfterLast("\\E", "")
        val regex = string.toRegex()
        if (lore.find { it.matches(regex) } == null) {
            itemStackNMS.tag!!.setInt("pro.eugw.toolstats.$type", 0)
        }
        var index = 0
        with(lore.find { it.matches(regex) }) {
            index = if (this == null) {
                lore.add(NBTTagString(config.getString("$type.lore").replace("&", "\u00a7")))
                lore.size() - 1
            } else {
                lore.indexOf(this)
            }
        }
        var count = itemStackNMS.tag!!.getInt("pro.eugw.toolstats.$type")
        count++
        for (i in 1..config.getString("$type.reward.count").split(",").size) {
            if (config.getBoolean("$type.reward.enabled") && count == config.getString("$type.reward.count").split(",")[i - 1].toInt() && player.hasPermission("toolstats.upgrade")) {
                if (config.getBoolean("$type.reward.enchant$i.enabled")) {
                    for (pre in config.getStringList("$type.reward.enchant$i.ench")) {
                        val ench = pre.split(":")
                        var list = try {
                            itemStackNMS.tag!!.getList("ench", 10)
                        } catch (e: Exception) {
                            itemStackNMS.tag!!.set("ench", NBTTagList())
                            itemStackNMS.tag!!.getList("ench", 10)
                        }
                        var itemEnchantment: NBTTagCompound? = null
                        var indexEnchantment = 0
                        for (x in 0 until list.size()) {
                            if (list.get(x).getInt("id") == enchantments[ench[0]]) {
                                itemEnchantment = list.get(x)
                                indexEnchantment = x
                                break
                            }
                        }
                        if (ench[1].toInt() > (itemEnchantment?.getInt("lvl") ?: -1)) {
                            if (itemEnchantment != null) {
                                itemEnchantment.setInt("lvl", ench[1].toInt())
                                list = list.a(indexEnchantment, itemEnchantment)
                            } else if (enchantments.containsKey(ench[0])) {
                                val eeeCompound = NBTTagCompound()
                                eeeCompound.setInt("id", enchantments[ench[0]]!!)
                                eeeCompound.setInt("lvl", ench[1].toInt())
                                list.add(eeeCompound)
                            }
                            itemStackNMS.tag!!.set("ench", list)
                        }
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
        lore = lore.a(index, NBTTagString(config.getString("$type.lore").replace("&", "\u00a7").replace("%count%", "$count")))
        val comp = NBTTagCompound()
        comp.set("Lore", lore)
        itemStackNMS.tag!!.set("display", comp)
        itemStackNMS.tag!!.setInt("pro.eugw.toolstats.$type", count)
        player.inventory.itemInHand = CraftItemStack.asCraftMirror(itemStackNMS)
    }

    fun setTrackingStatus(status: Boolean) {
        val itemStackNMS = CraftItemStack.asNMSCopy(player.inventory.itemInHand)
        if (itemStackNMS.tag == null)
            itemStackNMS.tag = NBTTagCompound()
        itemStackNMS.tag!!.setBoolean("pro.eugw.toolstats.tracking", status)
        player.inventory.itemInHand = CraftItemStack.asCraftMirror(itemStackNMS)
    }

    private fun NBTTagList.indexOf(s: String): Int {
        for (i in 0 until size()) if (f(i) == s) return i
        return -1
    }

    private fun NBTTagList.find(predicate: (String) -> Boolean): String? {
        for (i in 0 until size()) if (predicate(f(i))) return f(i)
        return null
    }

    private fun NBTTagList.a(index: Int, base: NBTBase): NBTTagList {
        val new = NBTTagList()
        for (i in 0 until size()) {
            if (i == index)
                new.add(base)
            else
                new.add(get(i))
        }
        return new
    }
}