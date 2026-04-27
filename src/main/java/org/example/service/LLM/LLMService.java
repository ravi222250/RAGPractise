package org.example.service.LLM;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

public interface LLMService {

    List<Double> getEmbedding(String text) throws IOException, ParseException;

    String ask (String query) throws IOException;
}
