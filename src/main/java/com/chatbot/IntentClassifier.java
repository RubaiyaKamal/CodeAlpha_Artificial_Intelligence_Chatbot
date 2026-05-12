package com.chatbot;

import com.chatbot.model.Intent;
import java.util.*;

/**
 * Classifies user input to an intent tag using TF-IDF + cosine similarity,
 * with a regex keyword fallback for short/common inputs.
 */
public class IntentClassifier {

    private static final double CONFIDENCE_THRESHOLD = 0.12;

    private final NLPProcessor nlp = new NLPProcessor();
    private final TFIDFVectorizer vectorizer = new TFIDFVectorizer();
    private final List<Intent> intents;
    private final List<String> labels = new ArrayList<>();
    private final List<double[]> docVectors = new ArrayList<>();

    public IntentClassifier(List<Intent> intents) {
        this.intents = intents;
        train();
    }

    private void train() {
        List<List<String>> docs = new ArrayList<>();
        for (Intent intent : intents) {
            for (String pattern : intent.getPatterns()) {
                docs.add(nlp.preprocess(pattern));
                labels.add(intent.getTag());
            }
        }
        vectorizer.fit(docs);
        for (int i = 0; i < docs.size(); i++) {
            docVectors.add(vectorizer.getDocVector(i));
        }
    }

    public String classify(String userInput) {
        // 1. TF-IDF cosine similarity
        List<String> tokens = nlp.preprocess(userInput);
        if (tokens.isEmpty()) tokens = nlp.tokenize(userInput);

        double[] query = vectorizer.transform(tokens);
        double best = -1;
        int bestIdx = -1;
        for (int i = 0; i < docVectors.size(); i++) {
            double sim = TFIDFVectorizer.cosineSimilarity(query, docVectors.get(i));
            if (sim > best) { best = sim; bestIdx = i; }
        }

        if (best >= CONFIDENCE_THRESHOLD && bestIdx >= 0) {
            return labels.get(bestIdx);
        }

        // 2. Regex keyword fallback
        String kw = keywordFallback(userInput.toLowerCase());
        return kw != null ? kw : "fallback";
    }

    private String keywordFallback(String s) {
        if (s.matches(".*(\\bhello\\b|\\bhi\\b|\\bhey\\b|howdy|good morning|good evening|good afternoon).*")) return "greeting";
        if (s.matches(".*(bye|goodbye|farewell|see you|ciao|take care|later).*")) return "farewell";
        if (s.matches(".*(thank|thanks|appreciate|cheers|thx).*")) return "thanks";
        if (s.matches(".*(your name|who are you|what are you|call you|introduce yourself|are you a bot|are you (an )?ai).*")) return "identity";
        if (s.matches(".*(\\bjoke\\b|make me laugh|funny|humor|humour|cheer me up).*")) return "joke";
        if (s.matches(".*(weather|temperature|forecast|raining|sunny|cloudy|umbrella).*")) return "weather";
        if (s.matches(".*(\\btime\\b|what time|current time|clock).*")) return "time";
        if (s.matches(".*(\\bdate\\b|today|what day|what date|calendar).*")) return "date";
        if (s.matches(".*(machine learning|\\bml\\b|deep learning|neural network|\\bai\\b|artificial intelligence|\\bnlp\\b).*")) return "ai";
        if (s.matches(".*(python|java|javascript|coding|programming|algorithm|software|developer).*")) return "programming";
        if (s.matches(".*(\\bhelp\\b|what can you do|capabilities|assist me|guide me).*")) return "help";
        if (s.matches(".*(sad|depress|anxious|stressed|lonely|unhappy|overwhelm|struggling).*")) return "mental_health";
        if (s.matches(".*(\\bmusic\\b|song|playlist|spotify|genre|artist|listen).*")) return "music";
        if (s.matches(".*(\\bmovie\\b|\\bfilm\\b|watch|cinema|netflix|streaming|series).*")) return "movies";
        if (s.matches(".*(\\bfood\\b|eat|hungry|recipe|cook|restaurant|cuisine).*")) return "food";
        if (s.matches(".*(sport|football|soccer|basketball|cricket|tennis|olympics|fifa|nba).*")) return "sports";
        if (s.matches(".*(\\bmath\\b|calculate|calculus|algebra|geometry|equation|arithmetic).*")) return "math";
        if (s.matches(".*(science|physics|chemistry|biology|astronomy|space|universe|gravity|dna).*")) return "science";
        if (s.matches(".*(technology|blockchain|cryptocurrency|5g|quantum|iot|virtual reality|augmented).*")) return "technology";
        if (s.matches(".*(how are you|how do you feel|are you okay|hows it going).*")) return "how_are_you";
        if (s.matches(".*(who (made|created|built|coded|programmed) you|your (creator|developer|programmer)).*")) return "creator";
        if (s.matches(".*(how old|your age|when (were you|did you) (born|created)).*")) return "age";
        if (s.matches(".*(awesome|great|wonderful|fantastic|excellent|amazing|brilliant|you.re (awesome|helpful)).*")) return "positive";
        if (s.matches(".*(stupid|useless|terrible|awful|hate you|you.re bad|worst bot).*")) return "negative";
        return null;
    }
}
