package org.example.service.retrieval;

import org.apache.hc.core5.http.ParseException;
import org.example.repository.InMemoryVectorStoreService;
import org.example.service.LLM.GeminiLLMService;

import java.io.IOException;
import java.util.List;

public class GeminiRetrievalService implements RetrievalService {

    public String retrieveSimilarVectors(String query, InMemoryVectorStoreService store, int topK) throws IOException, ParseException {
        List<Double> queryEmbedded = new GeminiLLMService().getEmbedding(query);

        List<String> contextChunks = store.search(queryEmbedded, topK);
        return String.join("\n", contextChunks);
    }
}
