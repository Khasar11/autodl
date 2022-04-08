package com.khasar.autodl.adl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;

public final class Adl extends JavaPlugin implements CommandExecutor {

    private static Adl plugin;
    private static FileConfiguration config;
    private static List<String> downloadList;
    private static ListIterator<String> dl;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();

        config = getConfig();
        this.getCommand("getADLFiles").setExecutor(this::onCommand);
        try {
            configDownloadList(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Adl getPlugin() {
        return plugin;
    }

    public static void configDownloadList(CommandSender player) throws IOException {
        downloadList = config.getStringList("download-list");
        dl= downloadList.listIterator();
        int count = 0;
        while (dl.hasNext()) {
            String s = dl.next();
            File writeFile = new File(s.substring(s.indexOf("|||") + 3));
            writeFile.getParentFile().mkdirs();
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
            count++;
        }
        if (player != null) player.sendMessage("Downloaded "+count+" Files");
        plugin.getServer().getConsoleSender().sendMessage("Downloaded "+count+" Files");
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (s.isOp()) {
            s.sendMessage("Retrieving files from plugin config");
            try {
                configDownloadList(s);
            } catch (IOException e) {
                e.printStackTrace();
                s.sendMessage("Errored with IOException, check console");
            }
            return true;
        }
        return false;
    }

}
