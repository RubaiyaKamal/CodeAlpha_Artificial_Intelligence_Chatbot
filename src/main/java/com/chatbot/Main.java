package com.chatbot;

import com.chatbot.gui.ChatWindow;
import com.chatbot.server.ChatServer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Desktop;
import java.net.URI;

public class Main {

    /** The port the web server actually bound to (-1 = failed). Read by ChatWindow. */
    public static volatile int webPort = -1;

    public static void main(String[] args) {
        startWebServer();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ChatWindow window = new ChatWindow();
            window.setVisible(true);
        });
    }

    // Scans ports 8080-8085 until one is free
    public static void startWebServer() {
        System.out.println("[JavaBot] Looking for a free port...");
        for (int port = 8080; port <= 8085; port++) {
            try {
                ChatServer server = new ChatServer(port);
                server.start();
                webPort = port;
                Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

                String url = "http://localhost:" + port;
                System.out.println("[JavaBot] Web UI ready  →  " + url);
                openBrowser(url);
                return;

            } catch (Exception e) {
                System.out.println("[JavaBot] Port " + port + " in use (" +
                    e.getClass().getSimpleName() + ": " + e.getMessage() + "), trying next...");
            }
        }
        System.err.println("[JavaBot] ERROR: Could not bind to any port (8080-8085).");
        System.err.println("[JavaBot] Close apps that use those ports and restart.");
    }

    private static void openBrowser(String url) {
        // 1. java.awt.Desktop (most reliable on Windows/Mac)
        try {
            if (Desktop.isDesktopSupported() &&
                    Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
        } catch (Exception e) {
            System.out.println("[JavaBot] Desktop.browse failed: " + e.getMessage());
        }

        // 2. OS shell fallback
        String os = System.getProperty("os.name", "").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }
        } catch (Exception e) {
            System.out.println("[JavaBot] Could not open browser. Visit manually: " + url);
        }
    }
}
