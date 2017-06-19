package pro.eugw.ToolStats;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import static pro.eugw.ToolStats.Main.au;
import static pro.eugw.ToolStats.Main.ver;

class v {
    static void cr(FileConfiguration fileConfiguration) {
        if (!au){
            return;
        }
        if (!Objects.equals(fileConfiguration.getString("settings.configVer"), ver)) {
            u.infoc("You need update config!");
            File configFile = new File("plugins" + File.separator + "ToolStats", "config.yml");
            File file2 = new File("plugins" + File.separator + "ToolStats", "config.backup.yml");
            if (file2.exists()) {
                file2.delete();
            }
            configFile.renameTo(file2);
            u.infoc("File " + configFile + " renamed to " + file2);
        }
    }

    static void ue() throws Exception {
        if (!au){
            return;
        }
        URL url = new URL("https://api.github.com/repos/EugW/ToolStats/releases/latest");
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String verL = IOUtils.toString(inputStream, "UTF-8").split(",")[6].split(":")[1].replace(String.valueOf(('"')), "");
        if (!Objects.equals(ver, verL)) {
            URL ur = new URL("https://github.com/EugW/ToolStats/releases/download/" + verL + "/ToolStats.jar");
            File file = new File("plugins" + File.separator + "ToolStats.jar");
            FileUtils.copyURLToFile(ur, file);
            u.infoc("Update downloaded. Now you need restart your server");
        }
    }
}