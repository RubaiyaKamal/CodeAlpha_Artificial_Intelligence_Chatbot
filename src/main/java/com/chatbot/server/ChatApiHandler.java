package com.chatbot.server;

import com.chatbot.ChatEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handles POST /api/chat — reads {"message":"..."} and returns {"response":"..."}.
 * Uses a tiny hand-written JSON parser to avoid external libraries.
 */
public class ChatApiHandler implements HttpHandler {

    private final ChatEngine engine;

    public ChatApiHandler(ChatEngine engine) {
        this.engine = engine;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        addCorsHeaders(ex);

        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }

        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            sendText(ex, 405, "Method Not Allowed");
            return;
        }

        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String message = extractJsonString(body, "message");

            String reply = engine.getResponse(message != null ? message : "");
            String json  = "{\"response\":" + jsonQuote(reply) + "}";

            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);
        } catch (Exception e) {
            sendText(ex, 500, "Internal Server Error");
        } finally {
            ex.close();
        }
    }

    // ---------------------------------------------------------------
    // Minimal JSON helpers  (no external library needed)
    // ---------------------------------------------------------------

    private String extractJsonString(String json, String key) {
        int keyPos = json.indexOf("\"" + key + "\"");
        if (keyPos < 0) return null;

        int colon = json.indexOf(':', keyPos);
        if (colon < 0) return null;

        int openQ = json.indexOf('"', colon + 1);
        if (openQ < 0) return null;

        StringBuilder sb = new StringBuilder();
        int i = openQ + 1;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char nx = json.charAt(i + 1);
                switch (nx) {
                    case '"':  sb.append('"');  i += 2; continue;
                    case '\\': sb.append('\\'); i += 2; continue;
                    case 'n':  sb.append('\n'); i += 2; continue;
                    case 'r':  sb.append('\r'); i += 2; continue;
                    case 't':  sb.append('\t'); i += 2; continue;
                    default:   sb.append(nx);   i += 2; continue;
                }
            }
            if (c == '"') break;
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    /** Wraps a Java string in JSON quotes, escaping special characters. */
    private String jsonQuote(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    private void addCorsHeaders(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendText(HttpExchange ex, int code, String msg) throws IOException {
        byte[] b = msg.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, b.length);
        ex.getResponseBody().write(b);
        ex.close();
    }
}
