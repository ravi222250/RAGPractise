package org.example.repository;

import org.example.service.Retrieval.similarityAlgo.CosineSimilarity;

import java.util.ArrayList;
import java.util.List;

public class InMemoryVectorStoreService {

    private List<VectorDBItem> items = new ArrayList<>();

    public List<VectorDBItem> addItem(String text, List<Double> embedding) {
        VectorDBItem item = new VectorDBItem(text, embedding);
        items.add(item);
        return items;
    }


    public List<String> search(List<Double> embeddedQuery, int topK) {
        // we need to compare all items in the db against the embeddedQuery
        // and then sort the result and then take the ones which ar nearest to the query
        // this is descending sort
        return items.stream()
                .sorted((o1, o2) ->
                    Double.compare(
                            new CosineSimilarity().compute(o2.getEmbedding(), embeddedQuery),
                            new CosineSimilarity().compute(o1.getEmbedding(), embeddedQuery))
                ).limit(topK)
                .map(VectorDBItem::getText)
                .toList();
    }


}
