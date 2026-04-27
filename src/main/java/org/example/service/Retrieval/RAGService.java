package org.example.service.Retrieval;

import org.apache.hc.core5.http.ParseException;
import org.example.repository.InMemoryVectorDBStore;

import java.io.IOException;

public interface RAGService {

    String callLLM (String query, InMemoryVectorDBStore store) throws IOException, ParseException;
}
