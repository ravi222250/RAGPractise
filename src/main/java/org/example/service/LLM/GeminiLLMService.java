package org.example.service.LLM;

import org.apache.hc.client5.http.fluent.Request;
import org.example.config.RAGProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GeminiLLMService implements LLMService {

    private final String API_KEY;

    public GeminiLLMService() throws IOException {
        Properties properties = RAGProperties.getInstance().getProperties();
        API_KEY = System.getenv("GEMINI_API_KEY");
    }

    @Override
    public List<Double> getEmbedding(String text) throws IOException {

        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("Gemini API Key is missing. Check your properties file.");
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent?key=" + API_KEY;

        String body = """
        {
          "content": {
            "parts": [
              {"text": "%s"}
            ]
          }
        }
        """.formatted(text.replace("\"", "\\\""));

        String response = Request.post(url)
                .bodyString(body, org.apache.hc.core5.http.ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();

        JSONObject json = new JSONObject(response);

        JSONArray values = json
                .getJSONObject("embedding")
                .getJSONArray("values");

        List<Double> embedding = new ArrayList<>();
        for (int i = 0; i < values.length(); i++) {
            embedding.add(values.getDouble(i));
        }

        return embedding;

    }

    @Override
    public String ask(String prompt) throws IOException {
        // 1. Update the URL to use gemini-3-flash
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + API_KEY;

        // 2. Updated body (Using JSONObject to avoid manual escaping issues)
        JSONObject textPart = new JSONObject().put("text", prompt);
        JSONArray parts = new JSONArray().put(textPart);
        JSONObject contents = new JSONObject().put("parts", parts);

        JSONObject root = new JSONObject();
        root.put("contents", new JSONArray().put(contents));

        String body = root.toString();

        String response = Request.post(url)
                .bodyString(body, org.apache.hc.core5.http.ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();

        JSONObject json = new JSONObject(response);

        // Response structure remains consistent with the previous versions
        return json
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }
}
