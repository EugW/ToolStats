package pro.eugw.ToolStats;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class p {
    static void b(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        if (fileConfiguration.getBoolean("break.reward.enabled") && count.equals(fileConfiguration.getInt("break.reward.count")) && player.hasPermission("toolstats.upgrade")) {
            if (fileConfiguration.getBoolean("break.reward.enchant.enabled")) {
                for (Object pre : fileConfiguration.getList("break.reward.enchant.ench")) {
                    String[] ench = pre.toString().split(":");
                    if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                        itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    }
                }
                if (fileConfiguration.getBoolean("break.reward.exp.enabled")) {
                    player.giveExp(fileConfiguration.getInt("break.reward.exp.lvl"));
                }
                if (fileConfiguration.getBoolean("break.reward.sound.enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("break.reward.sound.sound")), 1F, 1F);
                }
                if (fileConfiguration.getBoolean("break.reward.items.enabled")) {
                    for (Object pre : fileConfiguration.getList("break.reward.items.give")) {
                        String[] item = pre.toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                    }
                }
            }
        }

    }

    static void kp(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        if (fileConfiguration.getBoolean("kill.player.reward.enabled") && count == fileConfiguration.getInt("kill.player.reward.count") && player.hasPermission("toolstats.upgrade")) {
            if (fileConfiguration.getBoolean("kill.player.reward.enchant.enabled")) {
                for (Object pre : fileConfiguration.getList("kill.player.reward.enchant.ench")) {
                    String[] ench = pre.toString().split(":");
                    if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                        itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    }
                }
            }
            if (fileConfiguration.getBoolean("kill.player.reward.exp.enabled")) {
                player.giveExp(fileConfiguration.getInt("kill.player.reward.exp.lvl"));
            }
            if (fileConfiguration.getBoolean("kill.player.reward.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.player.reward.sound.sound")), 1F, 1F);
            }
            if (fileConfiguration.getBoolean("kill.player.reward.items.enabled")) {
                for (Object pre : fileConfiguration.getList("kill.player.reward.items.give")) {
                    String[] item = pre.toString().split(":");
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                }
            }
        }
    }

    static void km(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        if (fileConfiguration.getBoolean("kill.mob.reward.enabled") && count == fileConfiguration.getInt("kill.mob.reward.count") && player.hasPermission("toolstats.upgrade")) {
            if (fileConfiguration.getBoolean("kill.mob.reward.enchant.enabled")) {
                for (Object pre : fileConfiguration.getList("kill.mob.reward.enchant.ench")) {
                    String[] ench = pre.toString().split(":");
                    if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                        itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    }
                }
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.exp.enabled")) {
                player.giveExp(fileConfiguration.getInt("kill.mob.reward.exp.lvl"));
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.mob.reward.sound.sound")), 1F, 1F);
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.items.enabled")) {
                for (Object pre : fileConfiguration.getList("kill.mob.reward.items.give")) {
                    String[] item = pre.toString().split(":");
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                }
            }
        }
    }
}