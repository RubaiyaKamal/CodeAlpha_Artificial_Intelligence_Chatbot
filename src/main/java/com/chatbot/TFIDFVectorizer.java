package com.chatbot;

import java.util.*;

/**
 * TF-IDF vectorizer: fits on a corpus of token lists, then transforms
 * any query into a weighted vector for cosine-similarity comparison.
 */
public class TFIDFVectorizer {

    private List<String> vocabulary = new ArrayList<>();
    private final List<Map<String, Integer>> termFreqs = new ArrayList<>();
    private final Map<String, Double> idf = new HashMap<>();
    private int docCount = 0;

    public void fit(List<List<String>> documents) {
        docCount = documents.size();
        Set<String> vocabSet = new LinkedHashSet<>();

        for (List<String> doc : documents) {
            Map<String, Integer> tf = new HashMap<>();
            for (String term : doc) {
                vocabSet.add(term);
                tf.merge(term, 1, Integer::sum);
            }
            termFreqs.add(tf);
        }

        vocabulary = new ArrayList<>(vocabSet);

        // Document frequency for each term
        Map<String, Integer> df = new HashMap<>();
        for (Map<String, Integer> tf : termFreqs) {
            for (String term : tf.keySet()) df.merge(term, 1, Integer::sum);
        }

        // Smooth IDF: log((N+1)/(df+1)) + 1
        for (String term : vocabulary) {
            int d = df.getOrDefault(term, 0);
            idf.put(term, Math.log((double)(docCount + 1) / (d + 1)) + 1.0);
        }
    }

    /** Returns the TF-IDF vector for a stored training document by index. */
    public double[] getDocVector(int index) {
        Map<String, Integer> tf = termFreqs.get(index);
        return buildVector(tf);
    }

    /** Returns the TF-IDF vector for an arbitrary (unseen) token list. */
    public double[] transform(List<String> tokens) {
        Map<String, Integer> tf = new HashMap<>();
        for (String t : tokens) tf.merge(t, 1, Integer::sum);
        return buildVector(tf);
    }

    private double[] buildVector(Map<String, Integer> tf) {
        double[] vec = new double[vocabulary.size()];
        int maxF = tf.values().stream().mapToInt(v -> v).max().orElse(1);
        for (int i = 0; i < vocabulary.size(); i++) {
            String term = vocabulary.get(i);
            double termFreq = tf.getOrDefault(term, 0) / (double) maxF;
            vec[i] = termFreq * idf.getOrDefault(term, 0.0);
        }
        return vec;
    }

    public static double cosineSimilarity(double[] a, double[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i] * a[i];
            nb  += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0.0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    public int getDocCount() { return docCount; }
}
