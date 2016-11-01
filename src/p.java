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
                Integer i = 0;
                while (i < fileConfiguration.getList("break.reward.enchant.ench").size()) {
                    String[] ench = fileConfiguration.getList("break.reward.enchant.ench").get(i).toString().split(":");
                    itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    i++;
                }
                if (fileConfiguration.getBoolean("break.reward.exp.enabled")) {
                    player.giveExp(fileConfiguration.getInt("break.reward.exp.lvl"));
                }
                if (fileConfiguration.getBoolean("break.reward.sound.enabled")) {
                    player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("break.reward.sound.sound")), 1F, 1F);
                }
                if (fileConfiguration.getBoolean("break.reward.items.enabled")) {
                    i = 0;
                    while (i < fileConfiguration.getList("break.reward.items.give").size()) {
                        String[] item = fileConfiguration.getList("break.reward.items.give").get(i).toString().split(":");
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                        i++;
                    }
                }
            }
        }

    }

    static void k(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        if (fileConfiguration.getBoolean("kill.player.reward.enabled") && count == fileConfiguration.getInt("kill.player.reward.count") && player.hasPermission("toolstats.upgrade")) {
            if (fileConfiguration.getBoolean("kill.player.reward.enchant.enabled")) {
                Integer i = 0;
                while (i < fileConfiguration.getList("kill.player.reward.enchant.ench").size()) {
                    String[] ench = fileConfiguration.getList("kill.player.reward.enchant.ench").get(i).toString().split(":");
                    itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    i++;
                }
            }
            if (fileConfiguration.getBoolean("kill.player.reward.exp.enabled")) {
                player.giveExp(fileConfiguration.getInt("kill.player.reward.exp.lvl"));
            }
            if (fileConfiguration.getBoolean("kill.player.reward.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.player.reward.sound.sound")), 1F, 1F);
            }
            if (fileConfiguration.getBoolean("kill.player.reward.items.enabled")) {
                Integer i = 0;
                while (i < fileConfiguration.getList("kill.player.reward.items.give").size()) {
                    String[] item = fileConfiguration.getList("kill.player.reward.items.give").get(i).toString().split(":");
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                    i++;
                }
            }
        }
    }

    static void km(Integer count, ItemMeta itemMeta, Player player, FileConfiguration fileConfiguration) {
        if (fileConfiguration.getBoolean("kill.mob.reward.enabled") && count == fileConfiguration.getInt("kill.mob.reward.count") && player.hasPermission("toolstats.upgrade")) {
            if (fileConfiguration.getBoolean("kill.mob.reward.enchant.enabled")) {
                Integer i = 0;
                while (i < fileConfiguration.getList("kill.mob.reward.enchant.ench").size()) {
                    String[] ench = fileConfiguration.getList("kill.mob.reward.enchant.ench").get(i).toString().split(":");
                    itemMeta.addEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
                    i++;
                }
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.exp.enabled")) {
                player.giveExp(fileConfiguration.getInt("kill.mob.reward.exp.lvl"));
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(fileConfiguration.getString("kill.mob.reward.sound.sound")), 1F, 1F);
            }
            if (fileConfiguration.getBoolean("kill.mob.reward.items.enabled")) {
                Integer i = 0;
                while (i < fileConfiguration.getList("kill.mob.reward.items.give").size()) {
                    String[] item = fileConfiguration.getList("kill.mob.reward.items.give").get(i).toString().split(":");
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(item[0]), Integer.parseInt(item[1])));
                    i++;
                }
            }
        }
    }
}