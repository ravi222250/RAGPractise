package org.example.service.LLM;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.config.RAGProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OpenAILLMService implements LLMService {

    private final String OPENAI_API_KEY;

    public OpenAILLMService() throws IOException {
        Properties properties = RAGProperties.getInstance().getProperties();
        OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    }

    @Override
    public List<Double> getEmbedding(String text) throws IOException, ParseException {

        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty()) {
            throw new IllegalStateException("Open API Key is missing. Check your properties file.");
        }

        String endpoint = "https://api.openai.com/v1/embeddings";

        String body = """
                {
                  "input": "%s",
                  "model": "text-embedding-3-small"
                }
                """.formatted(text.replace("\"", "\\\""));

        HttpPost request = new HttpPost(endpoint);
        request.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(body));

        List<Double> embedding = new ArrayList<>();

        CloseableHttpClient client = HttpClients.createDefault();
        String json = client.execute(request, response ->
            EntityUtils.toString(response.getEntity())
        );
        JSONObject obj = new JSONObject(json);

        JSONArray embeddingArray = obj
                .getJSONArray("data")
                .getJSONObject(0)
                .getJSONArray("embedding");

        for (int i = 0; i < embeddingArray.length(); i++) {
            embedding.add(embeddingArray.getDouble(i));
        }

        return embedding;
    }

    @Override
    public String ask(String query) throws IOException {
        String endpoint = "https://api.openai.com/v1/chat/completions";

        String body = """
                {
                  "input": "%s",
                  "model": "gpt-4o-mini"
                }
                """.formatted(query.replace("\"", "\\\""));

        HttpPost request = new HttpPost(endpoint);
        request.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(body));

        CloseableHttpClient client = HttpClients.createDefault();
        return client.execute(request, response ->
                EntityUtils.toString(response.getEntity())
        );
    }
}
