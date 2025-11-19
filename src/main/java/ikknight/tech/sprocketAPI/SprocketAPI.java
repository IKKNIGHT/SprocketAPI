package ikknight.tech.sprocketAPI;

import ikknight.tech.sprocketAPI.commands.ReloadSprocketAPI;
import ikknight.tech.sprocketAPI.server.ApiServer;
import ikknight.tech.sprocketAPI.server.ServerSnapshot;
import ikknight.tech.sprocketAPI.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SprocketAPI extends JavaPlugin {
    public static double Tps = 20.0;
    PluginDescriptionFile pdfFile;
    // Bukkit.getServer().reload();
    public static volatile ServerSnapshot snapshot = new ServerSnapshot();
    private ApiServer api;

    public PluginDescriptionFile getPdfFile() {
        return pdfFile;
    }
    private long[] tickTimes = new long[600]; // Store timestamps for 600 ticks (30 seconds at 20 TPS)
    private int tickIndex = 0;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        this.pdfFile = getDescription();
        reloadConfiguration();
        runAPI();
        apiTick();

        // init commands
        this.getCommand("reloadsprocket").setExecutor(new ReloadSprocketAPI(this));
    }

    public void runAPI() {
        api = new ApiServer(this.getConfig().getInt(Constants.ConfigPaths.PORT));
        try {
            api.start();
            Bukkit.getServer().getConsoleSender().sendMessage("Configured at port : " + this.getConfig().getInt(Constants.ConfigPaths.PORT));
            Bukkit.getServer().getConsoleSender().sendMessage("The API Key is present in the config.yml");
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
    }

    public void apiTick() {

        // Every tick: record timestamp
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            tickTimes[tickIndex % tickTimes.length] = System.nanoTime();
            tickIndex++;

            snapshot.update(getServer(), Tps);

        }, 0L, 1L);

        // Every second: compute TPS
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            int targetTicks = 20;

            if (tickIndex < targetTicks) {
                Tps = 20.0;
                return;
            }

            int newest = (tickIndex - 1) % tickTimes.length;
            int oldest = (tickIndex - targetTicks) % tickTimes.length;

            long elapsed = tickTimes[newest] - tickTimes[oldest];
            Tps = targetTicks / (elapsed / 1_000_000_000.0);

            // Cap TPS at 20 because Minecraft never exceeds 20 logically
            if (Tps > 20.0) Tps = 20.0;

        }, 20L, 20L);
    }

    public void configureFile() {
        this.getConfig().addDefault(Constants.ConfigPaths.PORT, Constants.ConfigValues.PORT);
        this.getConfig().addDefault(Constants.ConfigPaths.APIKEY, Constants.ConfigValues.APIKEY);
    }

    public void reloadConfiguration() {
        reloadConfig();
        configureFile();
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown
        stopAPI();
    }

    public void stopAPI() {
        if (api != null) {
            api.stop();
        }
    }
}
