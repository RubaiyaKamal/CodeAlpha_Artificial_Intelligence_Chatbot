package com.chatbot.server;

import com.chatbot.ChatEngine;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Lightweight HTTP server (JDK built-in, no external deps).
 *
 *   GET  /           → serves web/index.html
 *   GET  /style.css  → serves web/style.css
 *   GET  /app.js     → serves web/app.js
 *   POST /api/chat   → {"message":"..."} → {"response":"..."}
 */
public class ChatServer {

    private final HttpServer httpServer;
    private final int port;

    public ChatServer(int port) throws IOException {
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 16);

        ChatEngine engine = new ChatEngine();

        // Register /api/chat BEFORE / so longest-prefix matching picks it first
        httpServer.createContext("/api/chat", new ChatApiHandler(engine));
        httpServer.createContext("/",         new StaticFileHandler());

        httpServer.setExecutor(null); // use the default executor
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    public int getPort() { return port; }
}
