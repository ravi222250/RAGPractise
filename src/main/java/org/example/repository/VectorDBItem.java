package org.example.repository;

import java.util.List;

public class VectorDBItem {
    private String text;
    private List<Double> embedding;

    public VectorDBItem(String text, List<Double> embedding) {
        this.text = text;
        this.embedding = embedding;
    }

    public String getText() {
        return text;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }
}
