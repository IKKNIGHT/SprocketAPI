package ikknight.tech.sprocketAPI.server;

import fi.iki.elonen.NanoHTTPD;

public class ApiServer extends NanoHTTPD {
    public ApiServer(int port) {
        super(port);
    }
}
