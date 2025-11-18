package ikknight.tech.sprocketAPI.server;

import fi.iki.elonen.NanoHTTPD;

public class ApiServer extends NanoHTTPD {
    public ApiServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        switch (uri){
            case "/api/health":
                return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Healthy!");
            default:
                return newFixedLengthResponse(Response.Status.NOT_FOUND,
                        NanoHTTPD.MIME_PLAINTEXT,
                        "Unknown endpoint");
        }
    }
}
