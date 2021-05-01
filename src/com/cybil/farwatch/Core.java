package com.cybil.farwatch;

import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    public static boolean running;

    @Override
    public void onEnable() {
        running = true;
    }

    @Override
    public void onDisable() {
        running = false;
    }

}
