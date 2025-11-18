package ikknight.tech.sprocketAPI;

import ikknight.tech.sprocketAPI.commands.ReloadSprocketAPI;
import ikknight.tech.sprocketAPI.server.ApiServer;
import ikknight.tech.sprocketAPI.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SprocketAPI extends JavaPlugin {
    PluginDescriptionFile pdfFile;

    private ApiServer api;

    public PluginDescriptionFile getPdfFile() {
        return pdfFile;
    }
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        this.pdfFile = getDescription();
        reloadConfiguration();
        runAPI();

        // init commands
        this.getCommand("reloadsprocket").setExecutor(new ReloadSprocketAPI(this));
    }
    public void runAPI(){
        api = new ApiServer(this.getConfig().getInt(Constants.ConfigPaths.PORT));
        try {
            api.start();
            Bukkit.getServer().getConsoleSender().sendMessage("Configured at port : " + this.getConfig().getInt(Constants.ConfigPaths.PORT));
            Bukkit.getServer().getConsoleSender().sendMessage("The API Key is present in the config.yml");
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
    }
    public void configureFile(){
        this.getConfig().addDefault(Constants.ConfigPaths.PORT,Constants.ConfigValues.PORT);
        this.getConfig().addDefault(Constants.ConfigPaths.APIKEY, Constants.ConfigValues.APIKEY);
    }
    public void reloadConfiguration(){
        reloadConfig();
        configureFile();
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown
        if (api != null){
            api.stop();
        }
    }
    public void stopAPI(){
        if (api != null){
            api.stop();
        }
    }
}
