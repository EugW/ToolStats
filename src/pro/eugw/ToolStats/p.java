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
        for (Integer i = 1; i <= fileConfiguration.getString("break.reward.count").split(",").length; i++) {
            if (fileConfiguration.getBoolean("break.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("break.reward.count").split(",")[i-1])) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("break.reward.enchant" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("break.reward.enchant" + i + ".ench")) {
                        String[] ench = pre.toString().split(":");
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true);
                        }
                    }
                    if (fileConfiguration.getBoolean("break.reward.exp" + i + ".enabled")) {
                        player.giveExp(fileConfiguration.getInt("break.reward.exp" + i + ".lvl"));
                    }
                    if (fileConfiguration.getBoolean("break.reward.items" + i + ".enabled")) {
                        for (Object pre : fileConfiguration.getList("break.reward.items" + i + ".give")) {
                            String[] item = pre.toString().split(":");
                            player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])));
                        }
                    }
                    if (fileConfiguration.getBoolean("break.reward.sound" + i + ".enabled")) {
                        player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("break.reward.sound" + i + ".sound")), 1F, 1F);
                    }
                }
            }
        }
    }

    static void kp(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        for (Integer i = 1; i <= fileConfiguration.getString("kill.player.reward.count").length(); i++) {
            if (fileConfiguration.getBoolean("kill.player.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("kill.player.reward.count").split(",")[i-1])) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("kill.player.reward.enchant" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("kill.player.reward.enchant" + i + ".ench")) {
                        String[] ench = pre.toString().split(":");
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true);
                        }
                    }
                }
                if (fileConfiguration.getBoolean("kill.player.reward.exp" + i + ".enabled")) {
                    player.giveExp(fileConfiguration.getInt("kill.player.reward.exp" + i + ".lvl"));
                }
                if (fileConfiguration.getBoolean("kill.player.reward.items" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("kill.player.reward.items" + i + ".give")) {
                        String[] item = pre.toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])));
                    }
                }
                if (fileConfiguration.getBoolean("kill.player.reward.sound" + i + ".enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.player.reward.sound" + i + ".sound")), 1F, 1F);
                }

            }
        }

    }

    static void km(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        for (Integer i = 1; i <= fileConfiguration.getString("kill.mob.reward.count").split(",").length; i++) {
            if (fileConfiguration.getBoolean("kill.mob.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("kill.mob.reward.count").split(",")[i-1])) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("kill.mob.reward.enchant" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("kill.mob.reward.enchant" + i + ".ench")) {
                        String[] ench = pre.toString().split(":");
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true);
                        }
                    }
                }
                if (fileConfiguration.getBoolean("kill.mob.reward.exp" + i + ".enabled")) {
                    player.giveExp(fileConfiguration.getInt("kill.mob.reward.exp" + i + ".lvl"));
                }
                if (fileConfiguration.getBoolean("kill.mob.reward.items" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("kill.mob.reward.items" + i + ".give")) {
                        String[] item = pre.toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])));
                    }
                }
                if (fileConfiguration.getBoolean("kill.mob.reward.sound" + i + ".enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.mob.reward.sound" + i + ".sound")), 1F, 1F);
                }
            }
        }
    }
}