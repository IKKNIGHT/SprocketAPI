package ikknight.tech.sprocketAPI.server;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import ikknight.tech.sprocketAPI.SprocketAPI;

public class ApiServer extends NanoHTTPD {

    private final Gson gson = new Gson();

    public ApiServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        switch (uri) {

            // Health check
            case "/api/health":
                return json("status", "healthy");

            // Full snapshot
            case "/api/snapshot":
                return json(SprocketAPI.snapshot);

            // TPS
            case "/api/tps":
                return json("tps",SprocketAPI.snapshot.getTps());

            // Online players
            case "/api/players":
                return json("players",SprocketAPI.snapshot.getOnlinePlayers());

            // Operators
            case "/api/operators":
                return json("operators",SprocketAPI.snapshot.getOperators());

            // Worlds
            case "/api/worlds":
                return json("worlds",SprocketAPI.snapshot.getWorlds());

            // Whitelisted players
            case "/api/whitelist":
                return json("whitelist",SprocketAPI.snapshot.getWhitelistedPlayers());

            // Banned players
            case "/api/banned":
                return json("banned",SprocketAPI.snapshot.getBannedPlayers());

            // Server settings
            case "/api/settings":
                return json(new ServerSettingsResponse(SprocketAPI.snapshot));

            // Network info
            case "/api/network":
                return json(new NetworkInfoResponse(SprocketAPI.snapshot));

            default:
                return newFixedLengthResponse(Response.Status.NOT_FOUND,
                        MIME_PLAINTEXT,
                        "Unknown endpoint");
        }
    }

    // ---- JSON Helpers ----

    private Response json(Object obj) {
        return newFixedLengthResponse(Response.Status.OK,
                "application/json",
                gson.toJson(obj));
    }

    private Response json(String key, Object value) {
        return json(new SimpleResponse(key, value));
    }

    // Wrapper for simple one-key JSON responses
    private static class SimpleResponse {
        public final Object value;
        public SimpleResponse(Object value) {
            this.value = value;
        }
        public SimpleResponse(String key, Object value) {
            this.value = value;
        }
    }

    // ---- Grouped response classes ----

    private static class ServerSettingsResponse {
        public final boolean allowEnd;
        public final boolean allowFlight;
        public final boolean allowNether;
        public final boolean generateStructures;
        public final boolean hardcore;
        public final boolean onlineMode;
        public final boolean hideOnlinePlayers;
        public final boolean hasWhiteList;

        public final int maxPlayers;
        public final int maxWorldSize;
        public final int maxChainedNeighborUpdates;
        public final int idleTimeout;
        public final int pauseWhenEmptyTime;

        public ServerSettingsResponse(ServerSnapshot s) {
            this.allowEnd = s.getAllowEnd();
            this.allowFlight = s.getAllowFlight();
            this.allowNether = s.getAllowNether();
            this.generateStructures = s.getGenerateStructures();
            this.hardcore = s.isHardcore();
            this.onlineMode = s.isOnlineMode();
            this.hideOnlinePlayers = s.getHideOnlinePlayers();
            this.hasWhiteList = s.getHasWhiteList();

            this.maxPlayers = s.getMaxPlayers();
            this.maxWorldSize = s.getMaxWorldSize();
            this.maxChainedNeighborUpdates = s.getMaxChainedNeighborUpdates();
            this.idleTimeout = s.getIdleTimeout();
            this.pauseWhenEmptyTime = s.getPauseWhenEmptyTime();
        }
    }

    private static class NetworkInfoResponse {
        public final String ip;
        public final int port;
        public final long connectionThrottle;

        public NetworkInfoResponse(ServerSnapshot s) {
            this.ip = s.getIp();
            this.port = s.getPort();
            this.connectionThrottle = s.getConnectionThrottle();
        }
    }
}
