package org.example.service.similarity;

import java.util.List;

public interface Similarity {

        Double compute (List<Double> vector1, List<Double> vector2);
}
