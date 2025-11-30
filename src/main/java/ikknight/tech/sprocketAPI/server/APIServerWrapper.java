package ikknight.tech.sprocketAPI.server;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public class APIServerWrapper {
    public enum Mutable{
        DEFAULTGAMEMODE,
        IDLETIMEOUT,
        MAXPLAYERS,
        MOTD,
        PAUSEWHENEMPTYTIME,
        SPANWRADIUS,
        WHITELIST,
        WHITELISTENFORCED
    }
    Server server;
    public void update(Server server){
        this.server = server;
    }
    
    public void update(@NotNull Mutable mutable, @NotNull Object value){
        if (mutable.equals(Mutable.DEFAULTGAMEMODE)){
            server.setDefaultGameMode((GameMode) value);
        }else if (mutable.equals(Mutable.IDLETIMEOUT)){
            server.setIdleTimeout((int) value);
        }else if (mutable.equals(Mutable.MAXPLAYERS)){
            server.setMaxPlayers((int) value);
        }else if (mutable.equals(Mutable.MOTD)){
            server.setMotd((String) value);
        }else if (mutable.equals(Mutable.PAUSEWHENEMPTYTIME)){
            server.setPauseWhenEmptyTime((int) value);
        }else if (mutable.equals(Mutable.SPANWRADIUS)){
            server.setSpawnRadius((int) value);
        }else if (mutable.equals(Mutable.WHITELIST)){
            server.setWhitelist((boolean) value);
        }else if (mutable.equals(Mutable.WHITELISTENFORCED)){
            server.setWhitelistEnforced((boolean) value);
        }
    }
}
