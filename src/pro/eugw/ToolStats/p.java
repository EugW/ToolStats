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
            if (fileConfiguration.getBoolean("break.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("break.reward.count").split(",")[i - 1])) && player.hasPermission("toolstats.upgrade")) {
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
        for (Integer i = 1; i <= fileConfiguration.getString("killPlayer.reward.count").split(",").length; i++) {
            if (fileConfiguration.getBoolean("killPlayer.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("killPlayer.reward.count").split(",")[i - 1])) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("killPlayer.reward.enchant" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("killPlayer.reward.enchant" + i + ".ench")) {
                        String[] ench = pre.toString().split(":");
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true);
                        }
                    }
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.exp" + i + ".enabled")) {
                    player.giveExp(fileConfiguration.getInt("killPlayer.reward.exp" + i + ".lvl"));
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.items" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("killPlayer.reward.items" + i + ".give")) {
                        String[] item = pre.toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])));
                    }
                }
                if (fileConfiguration.getBoolean("killPlayer.reward.sound" + i + ".enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("killPlayer.reward.sound" + i + ".sound")), 1F, 1F);
                }

            }
        }

    }

    static void km(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        for (Integer i = 1; i <= fileConfiguration.getString("killMob.reward.count").split(",").length; i++) {
            if (fileConfiguration.getBoolean("killMob.reward.enabled") && count.equals(Integer.valueOf(fileConfiguration.getString("killMob.reward.count").split(",")[i - 1])) && player.hasPermission("toolstats.upgrade")) {
                if (fileConfiguration.getBoolean("killMob.reward.enchant" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("killMob.reward.enchant" + i + ".ench")) {
                        String[] ench = pre.toString().split(":");
                        if (Integer.valueOf(ench[1]) > itemMeta.getEnchantLevel(Enchantment.getByName(ench[0]))) {
                            itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.valueOf(ench[1]), true);
                        }
                    }
                }
                if (fileConfiguration.getBoolean("killMob.reward.exp" + i + ".enabled")) {
                    player.giveExp(fileConfiguration.getInt("killMob.reward.exp" + i + ".lvl"));
                }
                if (fileConfiguration.getBoolean("killMob.reward.items" + i + ".enabled")) {
                    for (Object pre : fileConfiguration.getList("killMob.reward.items" + i + ".give")) {
                        String[] item = pre.toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.valueOf(item[1])));
                    }
                }
                if (fileConfiguration.getBoolean("killMob.reward.sound" + i + ".enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("killMob.reward.sound" + i + ".sound")), 1F, 1F);
                }
            }
        }
    }
}