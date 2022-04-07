package com.khasar.autodl.autodl;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;

public final class Autodl extends JavaPlugin {

    private Autodl plugin;
    private FileConfiguration config;
    List<String> downloadList;
    ListIterator<String> dl;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();

        config = getConfig();
        this.downloadList = config.getStringList("download-list");
        this.dl= downloadList.listIterator();
        try {
            configDownloadList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    public Autodl getPlugin() {
        return plugin;
    }

    public void configDownloadList() throws IOException {
        while (dl.hasNext()) {
            String s = dl.next();
            File writeFile = new File(s.substring(s.indexOf("|||") + 3));
            plugin.getServer().getConsoleSender().sendMessage(writeFile.getPath());
            try (BufferedInputStream in = new BufferedInputStream(new URL(s.substring(0, s.indexOf("|||"))).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(writeFile)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}