package com.chatbot.model;

import java.util.List;

public class Intent {
    private final String tag;
    private final List<String> patterns;
    private final List<String> responses;

    public Intent(String tag, List<String> patterns, List<String> responses) {
        this.tag = tag;
        this.patterns = patterns;
        this.responses = responses;
    }

    public String getTag() { return tag; }
    public List<String> getPatterns() { return patterns; }
    public List<String> getResponses() { return responses; }
}
