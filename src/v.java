import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.*;
import java.net.*;
import java.util.Objects;

class v {
    static void cr(String current, FileConfiguration fileConfiguration) {
        if (!Objects.equals(fileConfiguration.getString("version"), current)) {
            u.infoc("You need update config!");
            File configFile = new File("plugins" + File.separator + "ToolStats", "config.yml");
            File file2 = new File("plugins" + File.separator + "ToolStats", "config.backup.yml");
            if (file2.exists()) {
                file2.delete();
                u.infoc("File " + file2 + " deleted");
            }
            configFile.renameTo(file2);
            u.infoc("File " + configFile + " renamed to " + file2);
        }
    }

    static void ue(String current) throws Exception {
        u.infoc("Searching for updates...");
        URL url = new URL("https://api.github.com/repos/EugW/ToolStats/releases/latest");
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String verL = IOUtils.toString(inputStream, "UTF-8").split(",")[6].split(":")[1].replace(String.valueOf(('"')), "");
        if (Objects.equals(current, verL)) {
            u.infoc("You are using latest version! Great!");
        } else {
            u.infoc("Update found! Downloading...");
            URL ur = new URL("https://github.com/EugW/ToolStats/releases/download/" + verL + "/ToolStats.jar");
            File file = new File("plugins" + File.separator + "ToolStats.jar");
            FileUtils.copyURLToFile(ur, file);
            u.infoc("Now you need restart your server");
        }
    }
}