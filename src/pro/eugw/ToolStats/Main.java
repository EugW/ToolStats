package pro.eugw.ToolStats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static pro.eugw.ToolStats.p.b;
import static pro.eugw.ToolStats.p.km;
import static pro.eugw.ToolStats.p.kp;
import static pro.eugw.ToolStats.u.info;
import static pro.eugw.ToolStats.u.infoc;
import static pro.eugw.ToolStats.v.cr;
import static pro.eugw.ToolStats.v.ue;

public class Main extends JavaPlugin implements Listener {
    static FileConfiguration config;
    static String ver = "2.8";
    static boolean au = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        infoc("Loading...");
        infoc("Version: " + ver);
        cr();
        saveDefaultConfig();
        config = getConfig();
        infoc("Enabled");
        try {
            ue();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDisable() {
        infoc("Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("toolstats")) {
            if (sender.hasPermission("toolstats.reload")) {
                cr();
                saveDefaultConfig();
                reloadConfig();
                info(getConfig().getString("settings.reload"), sender);
                try {
                    ue();
                } catch (Exception ignored) {
                }
            } else {
                info(getConfig().getString("settings.noPerm"), sender);
            }
            return true;
        }
        return false;
    }

    @EventHandler
    void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("toolstats.counter")) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!getConfig().getList("break.tools").contains(itemStack.getType().toString())) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        if (itemMeta.getLore() == null) {
            lore.add("");
            lore.set(0, getConfig().getString("break.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();

        } else if (!itemMeta.getLore().toString().contains(getConfig().getString("break.lore").replace("&", "\u00a7"))) {
            Integer i = 0;
            while (i < itemMeta.getLore().size() + 1) {
                lore.add("");
                i++;
            }
            i = 0;
            while (i < itemMeta.getLore().size()) {
                lore.set(i, itemMeta.getLore().get(i));
                i++;
            }
            lore.set(itemMeta.getLore().size(), getConfig().getString("break.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        }
        Integer stratI = itemMeta.getLore().size() - 1;
        Integer i = 0;
        while (i < itemMeta.getLore().size()) {
            lore.add(itemMeta.getLore().get(i));
            i++;
        }
        String[] tag = lore.get(stratI).split(":");
        Integer cnt = Integer.valueOf(tag[1].replace("[", "").replace(" ", ""));
        Integer count = cnt + 1;
        b(count, itemMeta, player, getConfig());
        lore.set(stratI, getConfig().getString("break.lore").replace("&", "\u00a7") + ": " + count);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof Player)) {
            return;
        }
        Player player = event.getEntity().getKiller();
        if (!player.hasPermission("toolstats.counter")) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!getConfig().getList("kill.tools").contains(itemStack.getType().toString())) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        if (itemMeta.getLore() == null) {
            lore.add("");
            lore.add("");
            lore.set(0, getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": 0");
            lore.set(1, getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        } else if (!itemMeta.getLore().toString().contains(getConfig().getString("kill.player.lore").replace("&", "\u00a7")) && !itemMeta.getLore().toString().contains(getConfig().getString("kill.mob.lore").replace("&", "\u00a7"))) {
            Integer i = 0;
            while (i < itemMeta.getLore().size() + 2) {
                lore.add("");
                i++;
            }
            i = 0;
            while (i < itemMeta.getLore().size()) {
                lore.set(i, itemMeta.getLore().get(i));
                i++;
            }
            lore.set(itemMeta.getLore().size(), getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": 0");
            lore.set(itemMeta.getLore().size() + 1, getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        }
        Integer startI = itemMeta.getLore().size() - 2;
        Integer i = 0;
        while (i < itemMeta.getLore().size()) {
            lore.add(itemMeta.getLore().get(i));
            i++;
        }
        String[] tagp = lore.get(startI).split(":");
        String[] tage = lore.get(startI + 1).split(":");
        Integer cntp = Integer.valueOf(tagp[1].replace("]", "").replace(" ", ""));
        Integer cnte = Integer.valueOf(tage[1].replace("]", "").replace(" ", ""));
        Integer countp = cntp;
        Integer counte = cnte;
        if (event.getEntity() instanceof Player) {
            countp = cntp + 1;
            kp(countp, itemMeta, player, getConfig());
        } else {
            counte = cnte + 1;
            km(counte, itemMeta, player, getConfig());

        }
        lore.set(startI, getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": " + countp);
        lore.set(startI + 1, getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": " + counte);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}