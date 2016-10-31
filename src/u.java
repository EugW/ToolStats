import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

class u {
    static void info(String msg, CommandSender sender) {
        sender.sendMessage("\u00a77[\u00a7eToolStats\u00a77] " + msg);
    }

    static void infoc(String msg) {
        Bukkit.getConsoleSender().sendMessage("\u00a77[\u00a7eToolStats\u00a77] " + msg);
    }
}