import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;

class v {
    private static boolean re(String ver) throws Exception {
        Integer serverPort = 8089;
        InetAddress inetAddress = InetAddress.getByName("srv1.ew-corp.com");
        Socket socket = new Socket(inetAddress, serverPort);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        while (true) {
            dataOutputStream.writeUTF(ver);
            dataOutputStream.flush();
            return dataInputStream.readUTF().equals("+");
        }
    }

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
        if (re(current)) {
            u.infoc("You are using latest version! Great!");
        } else {
            u.infoc("Update found! Downloading...");
            URL url = new URL("https://srv1.ew-corp.com/ToolStats.jar");
            File file = new File("plugins" + File.separator + "ToolStats.jar");
            FileUtils.copyURLToFile(url, file);
            u.infoc("Now you need restart your server");
        }
    }
}