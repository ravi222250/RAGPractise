package org.example.service.retrieval.similarityAlgo;

import java.util.List;

public class CosineSimilarity implements Similarity {

    @Override
    public Double compute(List<Double> v1, List<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        Double num0 = 0.0;
        Double num1 = 0.0;
        Double num2 = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            if (v1.get(i) == null || v2.get(i) == null) {
                throw new IllegalArgumentException("Vectors must not contain null values");
            }

            num0 += v1.get(i) * v2.get(i);
            num1 += Math.pow(v1.get(i), 2);
            num2 += Math.pow(v2.get(i), 2);
        }

        return num0 / (Math.sqrt(num1) * Math.sqrt(num2));

    }

}
