package org.example.service.Retrieval;

import org.apache.hc.core5.http.ParseException;
import org.example.repository.InMemoryVectorDBStore;
import org.example.service.LLM.GeminiLLMService;
import org.example.service.LLM.OpenAILLMService;

import java.io.IOException;
import java.util.List;

public class GeminiRAGService implements RAGService {
    @Override
    public String callLLM(String query, InMemoryVectorDBStore store) throws IOException, ParseException {

        List<Double> queryEmbedded = new OpenAILLMService().getEmbedding(query);

        List<String> contextChunks = new InMemoryVectorDBStore().search(queryEmbedded, 3);
        String context = String.join("\n", contextChunks);

        String prompt = """
                Answer based on the context below:
        
                Context:
                %s
        
                Question:
                %s
                """.formatted(context, query);

        return new GeminiLLMService().ask(prompt);
    }
}
