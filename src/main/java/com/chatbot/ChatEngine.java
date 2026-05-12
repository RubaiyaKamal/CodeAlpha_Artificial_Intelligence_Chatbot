package com.chatbot;

import com.chatbot.model.Intent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Core chatbot engine: classifies input, handles dynamic responses
 * (time, date, math), and returns a randomly selected reply from the
 * matching intent.
 */
public class ChatEngine {

    private final List<Intent> intents;
    private final IntentClassifier classifier;
    private final Random rng = new Random();

    public ChatEngine() {
        this.intents = TrainingData.getIntents();
        this.classifier = new IntentClassifier(intents);
    }

    public String getResponse(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Please type something! I'm here to help.";
        }

        String input = userInput.trim();

        // Check for math expression before intent classification
        if (isMathExpression(input)) {
            return evaluateMath(input);
        }

        String intentTag = classifier.classify(input);
        return buildResponse(intentTag);
    }

    private String buildResponse(String tag) {
        if ("time".equals(tag)) {
            String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return "The current time is " + t + ".";
        }
        if ("date".equals(tag)) {
            String d = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"));
            return "Today is " + d + ".";
        }

        for (Intent intent : intents) {
            if (intent.getTag().equals(tag)) {
                List<String> responses = intent.getResponses();
                if (!responses.isEmpty()) {
                    String r = responses.get(rng.nextInt(responses.size()));
                    if (!r.startsWith("__")) return r;
                }
            }
        }

        return "I'm not sure how to respond to that. Try asking about AI, programming, science, sports, or just say hello!";
    }

    // ---------------------------------------------------------------
    // Math expression detection and evaluation
    // ---------------------------------------------------------------

    private boolean isMathExpression(String input) {
        // Matches expressions like "5 + 3", "10 * 4 / 2", "(3+4)*2", "100-57"
        return input.matches("[\\d\\s+\\-*/()%.^]+") &&
               input.matches(".*\\d.*[+\\-*/].*\\d.*");
    }

    private String evaluateMath(String expression) {
        try {
            String clean = expression.replaceAll("[^\\d+\\-*/().]", "").trim();
            if (clean.isEmpty()) return "Please enter a valid math expression, e.g. '5 + 3 * 2'.";
            double result = eval(clean);
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return "That expression results in an undefined value (e.g., division by zero).";
            }
            if (result == Math.floor(result)) {
                return "Result: " + (long) result;
            }
            return "Result: " + String.format("%.6f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
        } catch (Exception e) {
            return "I can handle simple arithmetic like '5 + 3' or '(10 * 4) / 2'. Try a simpler expression!";
        }
    }

    /** Recursive descent expression evaluator (no external libraries). */
    private double eval(final String expr) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int target) {
                while (ch == ' ') nextChar();
                if (ch == target) { nextChar(); return true; }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpr();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpr() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return  parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int start = pos;
                if (eat('(')) {
                    x = parseExpr();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(start, pos));
                } else {
                    throw new RuntimeException("Unexpected char: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }

    public void clearHistory() { /* no-op; history is stateless per message */ }
}
