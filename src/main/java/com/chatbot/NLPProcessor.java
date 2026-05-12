package com.chatbot;

import java.util.*;

/**
 * NLP pipeline: tokenize → remove stop words → stem
 */
public class NLPProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "an", "the", "is", "it", "in", "on", "at", "to", "for",
        "of", "and", "or", "but", "with", "by", "from", "as", "into",
        "i", "me", "my", "we", "our", "you", "your", "he", "she", "they",
        "this", "that", "these", "those", "was", "were", "be", "been",
        "have", "has", "had", "do", "does", "did", "will", "would",
        "could", "should", "may", "might", "can", "shall", "am", "are",
        "just", "also", "so", "if", "then", "than", "when", "about",
        "which", "who", "whom", "above", "after", "before", "please",
        "hey", "hi", "hello", "know", "think", "want", "get", "let"
    ));

    public List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) return Collections.emptyList();
        text = text.toLowerCase()
                   .replaceAll("[^a-z0-9\\s]", " ")
                   .replaceAll("\\s+", " ")
                   .trim();
        List<String> tokens = new ArrayList<>();
        for (String t : text.split(" ")) {
            if (!t.isEmpty()) tokens.add(t);
        }
        return tokens;
    }

    public List<String> removeStopWords(List<String> tokens) {
        List<String> result = new ArrayList<>();
        for (String t : tokens) {
            if (!STOP_WORDS.contains(t)) result.add(t);
        }
        return result;
    }

    public String stem(String word) {
        if (word == null || word.length() <= 3) return word;

        // Ordered longest-first to avoid partial matches
        String[][] rules = {
            {"ational", "ate"}, {"tional", "tion"}, {"ingness", ""},
            {"iveness", "ive"}, {"fulness", "ful"}, {"ousness", "ous"},
            {"ication", "ic"},  {"alism", "al"},     {"aliti", "al"},
            {"ness", ""},       {"ment", ""},         {"tion", "t"},
            {"edly", ""},       {"ingly", ""},        {"izer", "ize"},
            {"ical", ""},       {"able", ""},         {"ible", ""},
            {"ally", ""},       {"ful", ""},          {"ous", ""},
            {"ive", ""},        {"ize", ""},          {"ise", ""},
            {"ing", ""},        {"er", ""},           {"ed", ""},
            {"ly", ""}
        };

        for (String[] rule : rules) {
            if (word.endsWith(rule[0])) {
                String stem = word.substring(0, word.length() - rule[0].length()) + rule[1];
                if (stem.length() >= 3) return stem;
            }
        }

        if (word.endsWith("ies") && word.length() > 4)
            return word.substring(0, word.length() - 3) + "y";
        if (word.endsWith("es") && word.length() > 4)
            return word.substring(0, word.length() - 2);
        if (word.endsWith("s") && !word.endsWith("ss") && word.length() > 3)
            return word.substring(0, word.length() - 1);

        return word;
    }

    public List<String> preprocess(String text) {
        List<String> tokens = tokenize(text);
        tokens = removeStopWords(tokens);
        List<String> stemmed = new ArrayList<>();
        for (String t : tokens) {
            String s = stem(t);
            if (s.length() > 1) stemmed.add(s);
        }
        return stemmed;
    }
}
