package org.example.service.retrieval;

import org.apache.hc.core5.http.ParseException;
import org.example.repository.InMemoryVectorStoreService;

import java.io.IOException;

public interface RetrievalService {

    String retrieveSimilarVectors(String query, InMemoryVectorStoreService store, int topK) throws IOException, ParseException;
}
