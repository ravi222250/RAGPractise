package org.example.service.Retrieval;

import org.apache.hc.core5.http.ParseException;
import org.example.repository.InMemoryVectorStoreService;
import org.example.service.LLM.GeminiLLMService;
import org.example.service.LLM.OpenAILLMService;

import java.io.IOException;
import java.util.List;

public class GeminiRetrievalService implements RetrievalService {

    public String retrieveSimilarVectors(String query, InMemoryVectorStoreService store, int topK) throws IOException, ParseException {
        List<Double> queryEmbedded = new GeminiLLMService().getEmbedding(query);

        List<String> contextChunks = new InMemoryVectorStoreService().search(queryEmbedded, topK);
        String context = String.join("\n", contextChunks);
        return context;
    }
}
