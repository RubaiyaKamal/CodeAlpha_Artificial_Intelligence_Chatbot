package com.chatbot.gui;

import com.chatbot.ChatEngine;
import com.chatbot.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

/**
 * Modern dark-themed Swing chat interface.
 * Messages appear as rounded bubbles; bot replies arrive after
 * a short animated typing delay (simulated via SwingWorker).
 */
public class ChatWindow extends JFrame {

    // ---- Palette (dark theme) ----
    private static final Color BG         = new Color(15,  15,  30);
    private static final Color HEADER_BG  = new Color(22,  22,  45);
    private static final Color INPUT_BG   = new Color(30,  30,  58);
    private static final Color USER_BUBBLE = new Color(88,  86, 214);
    private static final Color BOT_BUBBLE  = new Color(32,  32,  62);
    private static final Color BORDER_CLR  = new Color(55,  55, 100);
    private static final Color TEXT        = new Color(235, 235, 250);
    private static final Color SUBTEXT     = new Color(140, 140, 165);
    private static final Color SEND_BTN    = new Color(88,  86, 214);
    private static final Color ONLINE_DOT  = new Color(52, 211, 153);

    private static final Font FONT_MAIN   = new Font("Segoe UI", Font.PLAIN,  14);
    private static final Font FONT_BOLD   = new Font("Segoe UI", Font.BOLD,   14);
    private static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN,  11);
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,   16);

    private final ChatEngine engine = new ChatEngine();

    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;

    public ChatWindow() {
        initFrame();
        initLayout();
        addWelcomeMessage();
    }

    // ---------------------------------------------------------------
    // Frame bootstrap
    // ---------------------------------------------------------------

    private void initFrame() {
        // Update title once the web server port is known (may still be -1 here)
        updateTitle();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 700);
        setMinimumSize(new Dimension(580, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
    }

    public void updateTitle() {
        int port = Main.webPort;
        if (port > 0) {
            setTitle("JavaBot Desktop  ·  Web: http://localhost:" + port);
        } else {
            setTitle("JavaBot - AI Chatbot");
        }
    }

    private void initLayout() {
        add(buildHeader(),   BorderLayout.NORTH);
        add(buildChatArea(), BorderLayout.CENTER);
        add(buildInput(),    BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Header bar
    // ---------------------------------------------------------------

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(10, 18, 10, 18)
        ));

        // --- left: avatar + name + status ---
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setBackground(HEADER_BG);

        JLabel avatar = new JLabel("AI") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(USER_BUBBLE);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("AI",
                    (getWidth()  - fm.stringWidth("AI")) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));

        JPanel nameBox = new JPanel(new GridLayout(2, 1, 0, 1));
        nameBox.setBackground(HEADER_BG);

        JLabel name = new JLabel("JavaBot");
        name.setFont(FONT_TITLE);
        name.setForeground(TEXT);

        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        statusRow.setBackground(HEADER_BG);
        JLabel dot = new JLabel("●");   // filled circle
        dot.setFont(new Font("Arial", Font.PLAIN, 10));
        dot.setForeground(ONLINE_DOT);
        JLabel statusTxt = new JLabel("Online  |  NLP Powered");
        statusTxt.setFont(FONT_SMALL);
        statusTxt.setForeground(SUBTEXT);
        statusRow.add(dot);
        statusRow.add(statusTxt);

        nameBox.add(name);
        nameBox.add(statusRow);

        left.add(avatar);
        left.add(nameBox);

        // --- right: action buttons ---
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setBackground(HEADER_BG);

        right.add(buildHeaderBtn("Clear", e -> clearChat()));
        right.add(buildHeaderBtn("Help",  e -> showHelp()));

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JButton buildHeaderBtn(String label, ActionListener al) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isRollover()
                    ? new Color(88, 86, 214, 180)
                    : new Color(55, 55, 100, 140);
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(TEXT);
                g2.setFont(FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(62, 30));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }

    // ---------------------------------------------------------------
    // Chat scroll area
    // ---------------------------------------------------------------

    private JScrollPane buildChatArea() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(BG);
        messagesPanel.setBorder(new EmptyBorder(12, 14, 12, 14));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar vbar = scrollPane.getVerticalScrollBar();
        vbar.setBackground(HEADER_BG);
        vbar.setPreferredSize(new Dimension(6, 0));
        vbar.setUnitIncrement(18);

        return scrollPane;
    }

    // ---------------------------------------------------------------
    // Input bar
    // ---------------------------------------------------------------

    private JPanel buildInput() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(HEADER_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(12, 18, 12, 18)
        ));

        // Rounded text field
        inputField = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        inputField.setOpaque(false);
        inputField.setForeground(TEXT);
        inputField.setCaretColor(new Color(160, 160, 220));
        inputField.setFont(FONT_MAIN);
        inputField.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Placeholder text
        installPlaceholder(inputField, "Type a message and press Enter...");
        inputField.addActionListener(e -> sendMessage());

        // Send button
        JButton sendBtn = new JButton("Send") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover()
                    ? SEND_BTN.brighter()
                    : SEND_BTN;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);
                g2.setColor(Color.WHITE);
                g2.setFont(FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("Send",
                    (getWidth()  - fm.stringWidth("Send")) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        sendBtn.setPreferredSize(new Dimension(90, 44));
        sendBtn.setOpaque(false);
        sendBtn.setContentAreaFilled(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> sendMessage());

        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendBtn,    BorderLayout.EAST);
        return panel;
    }

    private void installPlaceholder(JTextField field, String ph) {
        field.setText(ph);
        field.setForeground(SUBTEXT);
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(ph)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(ph);
                    field.setForeground(SUBTEXT);
                }
            }
        });
    }

    // ---------------------------------------------------------------
    // Messaging logic
    // ---------------------------------------------------------------

    private static final String PLACEHOLDER = "Type a message and press Enter...";

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || text.equals(PLACEHOLDER)) return;

        inputField.setText("");
        inputField.setForeground(TEXT);

        appendMessage(text, true);

        JPanel typingRow = appendTypingDots();

        String userText = text;
        new SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {
                Thread.sleep(500 + (int)(Math.random() * 700));
                return engine.getResponse(userText);
            }
            @Override protected void done() {
                messagesPanel.remove(typingRow);
                try {
                    appendMessage(get(), false);
                } catch (InterruptedException | ExecutionException ex) {
                    appendMessage("Sorry, an error occurred. Please try again.", false);
                }
                refreshPanel();
                scrollToBottom();
            }
        }.execute();
    }

    private void appendMessage(String text, boolean isUser) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setBorder(new EmptyBorder(5, 0, 3, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel bubble = buildBubble(text, isUser);
        JPanel bubbleWrapper = new JPanel(new FlowLayout(
            isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        bubbleWrapper.setBackground(BG);
        bubbleWrapper.add(bubble);

        String ts = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        JLabel time = new JLabel(ts);
        time.setFont(FONT_SMALL);
        time.setForeground(SUBTEXT);
        time.setBorder(isUser
            ? new EmptyBorder(0, 0, 0, 6)
            : new EmptyBorder(0, 6, 0, 0));

        JPanel timeRow = new JPanel(new FlowLayout(
            isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        timeRow.setBackground(BG);
        timeRow.add(time);

        row.add(bubbleWrapper, BorderLayout.CENTER);
        row.add(timeRow,       BorderLayout.SOUTH);

        messagesPanel.add(row);
        refreshPanel();
        scrollToBottom();
    }

    private JPanel buildBubble(String text, boolean isUser) {
        Color bg = isUser ? USER_BUBBLE : BOT_BUBBLE;

        JTextArea area = new JTextArea(text);
        area.setFont(FONT_MAIN);
        area.setForeground(TEXT);
        area.setOpaque(false);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(null);
        area.setColumns(32);     // drives preferred width; wraps naturally

        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(new EmptyBorder(10, 14, 10, 14));
        bubble.add(area, BorderLayout.CENTER);
        bubble.setMaximumSize(new Dimension(480, Integer.MAX_VALUE));

        return bubble;
    }

    private JPanel appendTypingDots() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setBorder(new EmptyBorder(5, 0, 3, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel dots = new JLabel("  typing...  ") {
            private int frame = 0;
            private final Timer timer = new Timer(400, e -> {
                frame = (frame + 1) % 4;
                String d = ".".repeat(frame + 1);
                setText("  typing" + d + "  ");
            });
            { timer.start(); }
        };
        dots.setFont(FONT_SMALL);
        dots.setForeground(SUBTEXT);

        JPanel dotBubble = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BOT_BUBBLE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        dotBubble.setOpaque(false);
        dotBubble.setBorder(new EmptyBorder(8, 14, 8, 14));
        dotBubble.add(dots);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(BG);
        wrapper.add(dotBubble);
        row.add(wrapper, BorderLayout.CENTER);

        messagesPanel.add(row);
        refreshPanel();
        scrollToBottom();
        return row;
    }

    // ---------------------------------------------------------------
    // Welcome, clear, help
    // ---------------------------------------------------------------

    private void addWelcomeMessage() {
        int port = Main.webPort;
        String webLine = (port > 0)
            ? "Web interface: http://localhost:" + port + "  (open in browser)\n\n"
            : "Web interface: not running (port 8080-8085 all in use)\n\n";

        appendMessage(
            "Hello! I'm JavaBot, your AI-powered assistant.\n\n" +
            webLine +
            "I understand natural language using TF-IDF and cosine\n" +
            "similarity. You can ask me about:\n\n" +
            "  AI & Machine Learning  |  Programming\n" +
            "  Science & Space        |  Sports\n" +
            "  Movies & Music         |  Food\n" +
            "  Math (type: 5+3*2)     |  Jokes\n\n" +
            "Type 'help' to see more, or just start chatting!",
            false
        );
    }

    private void clearChat() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Clear all messages?", "Clear Chat",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            messagesPanel.removeAll();
            engine.clearHistory();
            addWelcomeMessage();
            refreshPanel();
        }
    }

    private void showHelp() {
        String msg =
            "JavaBot can answer questions and chat about:\n\n" +
            " Greetings & farewells\n" +
            " Artificial Intelligence & NLP\n" +
            " Java & Programming concepts\n" +
            " Science, Physics, Biology, Space\n" +
            " Technology trends (blockchain, 5G, AI)\n" +
            " Movies, Music, Food, Sports\n" +
            " Mental health support\n" +
            " Math expressions (e.g., 10 * (3 + 4))\n" +
            " Current time & date\n" +
            " Jokes\n\n" +
            "Just type naturally - I use TF-IDF NLP to understand you!";
        JOptionPane.showMessageDialog(this, msg, "JavaBot Help", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------------------------------------------------------------
    // Utilities
    // ---------------------------------------------------------------

    private void refreshPanel() {
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vbar = scrollPane.getVerticalScrollBar();
            vbar.setValue(vbar.getMaximum());
        });
    }
}
