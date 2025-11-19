package ikknight.tech.sprocketAPI.server;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerSnapshot {

    // ===== General Info =====
    private long timestamp;
    private String name;
    private String motd;
    private String bukkitVersion;
    private boolean hardcore;
    private boolean onlineMode;

    // ===== World / Gameplay Settings =====
    private boolean allowEnd;
    private boolean allowFlight;
    private boolean allowNether;
    private boolean generateStructures;

    private int maxWorldSize;
    private int maxChainedNeighborUpdates;
    private int maxPlayers;
    private boolean hideOnlinePlayers;
    private boolean hasWhiteList;
    private int idleTimeout;
    private int pauseWhenEmptyTime;

    // ===== Network =====
    private String ip;
    private int port;
    private long connectionThrottle;

    // ===== Dynamic Data =====
    private double tps;

    private List<String> onlinePlayers;
    private Set<String> operators;
    private Set<String> whitelistedPlayers;
    private Set<String> bannedPlayers;
    private List<String> worlds;

    // ============================================================
    //                     UPDATE SNAPSHOT
    // ============================================================

    /**
     * Must be called on the Bukkit main thread!
     */
    public void update(Server server, double currentTps) {
        this.timestamp = System.currentTimeMillis();
        this.tps = currentTps;

        // ---- General info ----
        this.name = server.getName();
        this.motd = server.getMotd();
        this.bukkitVersion = server.getBukkitVersion();
        this.hardcore = server.isHardcore();
        this.onlineMode = server.getOnlineMode();

        // ---- Gameplay settings ----
        this.allowEnd = server.getAllowEnd();
        this.allowFlight = server.getAllowFlight();
        this.allowNether = server.getAllowNether();
        this.generateStructures = server.getGenerateStructures();
        this.maxWorldSize = server.getMaxWorldSize();
        this.maxChainedNeighborUpdates = server.getMaxChainedNeighborUpdates();
        this.maxPlayers = server.getMaxPlayers();
        this.hideOnlinePlayers = server.getHideOnlinePlayers();
        this.hasWhiteList = server.hasWhitelist();
        this.idleTimeout = server.getIdleTimeout();
        this.pauseWhenEmptyTime = server.getPauseWhenEmptyTime();

        // ---- Network ----
        this.ip = server.getIp();
        this.port = server.getPort();
        this.connectionThrottle = server.getConnectionThrottle();

        // ---- Online players ----
        this.onlinePlayers = server.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        // ---- Operators ----
        this.operators = server.getOperators().stream()
                .map(OfflinePlayer::getName)
                .collect(Collectors.toSet());

        // ---- Whitelist ----
        this.whitelistedPlayers = server.getWhitelistedPlayers().stream()
                .map(OfflinePlayer::getName)
                .collect(Collectors.toSet());

        // ---- Bans ----
        this.bannedPlayers = server.getBannedPlayers().stream()
                .map(OfflinePlayer::getName)
                .collect(Collectors.toSet());

        // ---- Worlds ----
        this.worlds = server.getWorlds().stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    // ============================================================
    //                     GETTERS (NO SETTERS)
    // ============================================================

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getMotd() {
        return motd;
    }

    public String getBukkitVersion() {
        return bukkitVersion;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public boolean getAllowEnd() {
        return allowEnd;
    }

    public boolean getAllowFlight() {
        return allowFlight;
    }

    public boolean getAllowNether() {
        return allowNether;
    }

    public boolean getGenerateStructures() {
        return generateStructures;
    }

    public int getMaxWorldSize() {
        return maxWorldSize;
    }

    public int getMaxChainedNeighborUpdates() {
        return maxChainedNeighborUpdates;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean getHideOnlinePlayers() {
        return hideOnlinePlayers;
    }

    public boolean getHasWhiteList() {
        return hasWhiteList;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public int getPauseWhenEmptyTime() {
        return pauseWhenEmptyTime;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public long getConnectionThrottle() {
        return connectionThrottle;
    }

    public double getTps() {
        return tps;
    }

    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public Set<String> getOperators() {
        return operators;
    }

    public Set<String> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    public Set<String> getBannedPlayers() {
        return bannedPlayers;
    }

    public List<String> getWorlds() {
        return worlds;
    }
}
