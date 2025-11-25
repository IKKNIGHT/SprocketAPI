package ikknight.tech.sprocketAPI.server;

import org.bukkit.GameMode;
import org.bukkit.Server;

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
    public void update(Mutable mutable, Object value){
        if (mutable.equals(Mutable.DEFAULTGAMEMODE)){
            server.setDefaultGameMode((GameMode) value);
        }
    }
}
