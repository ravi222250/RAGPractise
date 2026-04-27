package org.example;

import lombok.SneakyThrows;
import org.example.repository.InMemoryVectorStoreService;
import org.example.service.Chunking.TextChunkingService;
import org.example.service.LLM.GeminiLLMService;
import org.example.service.LLM.LLMService;
import org.example.service.Retrieval.RetrievalService;
import org.example.service.reader.PDFService;

import java.util.ArrayList;
import java.util.List;

public class main {

    @SneakyThrows
    public static void main(String[] args) throws Exception {
        // ---------------Read PDF
        PDFService pdfService = new PDFService();
        String cadenza = pdfService.readPDF("src/main/resources/cadenza.pdf");
        String greenage = pdfService.readPDF("src/main/resources/greenage.pdf");
        String zoo = pdfService.readPDF("src/main/resources/zoo.pdf");


        // -------------------chunking
        TextChunkingService chunkingService = new TextChunkingService();
        List<String> cadenzaChunks = chunkingService.chunkText(cadenza, 100);
        List<String> greenageChunks = chunkingService.chunkText(greenage, 100);
        List<String> zooChunks = chunkingService.chunkText(zoo, 100);

        List<String> allChunks = new ArrayList<>();
        allChunks.addAll(cadenzaChunks);
        allChunks.addAll(greenageChunks);
        allChunks.addAll(zooChunks);

        //-------------------- embedding and storing in vector db for cadenza
        LLMService llmService = new GeminiLLMService();
        //LLMService llmService = new OpenAILLMService();
        InMemoryVectorStoreService vectorStoreService = new InMemoryVectorStoreService();

        for (String chunk: allChunks) {
            List<Double> embedding = llmService.getEmbedding(chunk);
            vectorStoreService.addItem(chunk, embedding);
        }


        ///............... Going to the question/retrieval part now---------------
        RetrievalService retrievalService = new RetrievalService();
        String question = "What is the cadenza?";
        String context = retrievalService.retrieveSimilarVectors(question, vectorStoreService, 2);
        System.out.println("Answer(which will serve as context for actual LLM: " + context);


        // using the answer from above as context for the same question but this time asking the actual LLM
        String query = """
                Answer the question based on the following context:
                
                Context: %s
                
                Question: %s
                """.formatted(context, question);
        String finalAnswer = llmService.ask(query);
        System.out.println("Final Answer from LLM: " + finalAnswer);
    }
}
