package ikknight.tech.sprocketAPI;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SprocketAPI extends JavaPlugin {
    FileConfiguration config = this.getConfig();
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        config.addDefault("config", 8080); // defaulting to 8080 here.
        Bukkit.getServer().getConsoleSender().sendMessage("Configured at port : " + config.getInt("port"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
