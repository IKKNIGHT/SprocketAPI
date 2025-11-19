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
    public static double Tps;
    PluginDescriptionFile pdfFile;
    // Bukkit.getServer().reload();
    public static volatile ServerSnapshot snapshot;
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

    public void apiTick(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            // schedule tasks to run every tick (1/20 of a second), run lightweight
            // tasks here.
            tickTimes[tickIndex % tickTimes.length] = System.currentTimeMillis();
            tickIndex++;
            snapshot.update(getServer(),Tps);
        }, 0L, 1L); // Run every tick
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            // schedule tasks to run every second, run heavy tasks here
            int targetTicks = 20; // Calculate TPS over the last 20 ticks
            if (tickIndex < targetTicks) {
                Tps = 20.0; // Not enough data yet, assume perfect TPS
            }

            long oldestTickTime = tickTimes[(tickIndex - targetTicks) % tickTimes.length];
            long latestTickTime = tickTimes[(tickIndex - 1) % tickTimes.length];

            double elapsedMillis = latestTickTime - oldestTickTime;
            Tps = (double) targetTicks / (elapsedMillis / 1000.0);
        }, 200L, 20L); // Calculate every second (20 ticks)
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
