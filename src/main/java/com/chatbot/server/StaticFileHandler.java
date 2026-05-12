package com.chatbot.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Serves static files from the classpath under /web/.
 * Maps URL paths to files bundled inside the JAR at /web/<file>.
 */
public class StaticFileHandler implements HttpHandler {

    private static final Map<String, String> MIME = Map.of(
        "html", "text/html; charset=UTF-8",
        "css",  "text/css; charset=UTF-8",
        "js",   "application/javascript; charset=UTF-8",
        "ico",  "image/x-icon",
        "png",  "image/png",
        "svg",  "image/svg+xml"
    );

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if ("/".equals(path)) path = "/index.html";

        // Only serve GET
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }

        InputStream is = getClass().getResourceAsStream("/web" + path);
        if (is == null) {
            byte[] msg = ("404 – " + path + " not found").getBytes();
            ex.sendResponseHeaders(404, msg.length);
            ex.getResponseBody().write(msg);
            ex.close();
            return;
        }

        byte[] content = is.readAllBytes();
        is.close();

        String ext  = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : "";
        String mime = MIME.getOrDefault(ext, "application/octet-stream");

        ex.getResponseHeaders().set("Content-Type", mime);
        ex.getResponseHeaders().set("Cache-Control", "no-cache");
        ex.sendResponseHeaders(200, content.length);
        ex.getResponseBody().write(content);
        ex.close();
    }
}
