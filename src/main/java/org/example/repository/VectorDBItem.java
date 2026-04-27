package org.example.repository;

import lombok.Getter;

import java.util.List;

@Getter
public class VectorDBItem {
    private String text;
    private List<Double> embedding;

    public VectorDBItem(String text, List<Double> embedding) {
        this.text = text;
        this.embedding = embedding;
    }
}
