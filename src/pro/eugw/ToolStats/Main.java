package pro.eugw.ToolStats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static pro.eugw.ToolStats.p.*;
import static pro.eugw.ToolStats.u.info;
import static pro.eugw.ToolStats.u.infoc;
import static pro.eugw.ToolStats.v.cr;
import static pro.eugw.ToolStats.v.ue;

public class Main extends JavaPlugin implements Listener {

    static String ver = null;
    static boolean au = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        ver = this.getDescription().getVersion();
        au = this.getConfig().getBoolean("settings.autoUpdate");
        infoc("Version: " + ver + " enabled. AutoUpdate: " + au);
        cr(getConfig());
        saveDefaultConfig();
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
                cr(getConfig());
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
        if (!getConfig().getBoolean("break.enabled")){
            return;
        }
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
            lore.add(getConfig().getString("break.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        } else if (!itemMeta.getLore().toString().contains(getConfig().getString("break.lore").replace("&", "\u00a7"))) {
            lore.addAll(itemMeta.getLore());
            lore.add(getConfig().getString("break.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        }
        lore.addAll(itemMeta.getLore());
        for (String pre : lore) {
            if (pre.contains(getConfig().getString("break.lore").replace("&", "\u00a7"))){
                Integer cnt = Integer.valueOf(pre.replaceAll("[\\D]", ""));
                cnt++;
                b(cnt, itemMeta, player, getConfig());
                lore.set(lore.indexOf(pre), getConfig().getString("break.lore").replace("&", "\u00a7") + ": " + cnt);
            }
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!getConfig().getBoolean("kill.enabled")){
            return;
        }
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
            lore.add(getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": 0");
            lore.add(getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        } else if (!itemMeta.getLore().toString().contains(getConfig().getString("kill.player.lore").replace("&", "\u00a7")) && !itemMeta.getLore().toString().contains(getConfig().getString("kill.mob.lore").replace("&", "\u00a7"))) {
            lore.addAll(itemMeta.getLore());
            lore.add(getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": 0");
            lore.add(getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": 0");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
        }
        lore.addAll(itemMeta.getLore());
        for (String pre : lore) {
            if (event.getEntity() instanceof Player) {
                if (pre.contains(getConfig().getString("kill.player.lore").replace("&", "\u00a7"))){
                    Integer cnt = Integer.valueOf(pre.replaceAll("[\\D]", ""));
                    cnt++;
                    kp(cnt, itemMeta, player, getConfig());
                    lore.set(lore.indexOf(pre), getConfig().getString("kill.player.lore").replace("&", "\u00a7") + ": " + cnt);
                }
            } else {
                if (pre.contains(getConfig().getString("kill.mob.lore").replace("&", "\u00a7"))){
                    Integer cnt = Integer.valueOf(pre.replaceAll("[\\D]", ""));
                    cnt++;
                    km(cnt, itemMeta, player, getConfig());
                    lore.set(lore.indexOf(pre), getConfig().getString("kill.mob.lore").replace("&", "\u00a7") + ": " + cnt);
                }
            }
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}