package org.example;

import lombok.SneakyThrows;
import org.example.repository.InMemoryVectorStoreService;
import org.example.service.Chunking.TextChunkingService;
import org.example.service.LLM.GeminiLLMService;
import org.example.service.LLM.LLMService;
import org.example.service.retrieval.GeminiRetrievalService;
import org.example.service.retrieval.RetrievalService;
import org.example.service.reader.PDFService;

import java.util.ArrayList;
import java.util.List;

public class main {

    @SneakyThrows
    public static void main(String[] args) throws Exception {
        // ---------------Read PDF
        PDFService pdfService = new PDFService();
        String cadenza = pdfService.readPDF("src/main/resources/pdfs/cadenza.pdf");
        String greenage = pdfService.readPDF("src/main/resources/pdfs/greenage.pdf");
        String zoo = pdfService.readPDF("src/main/resources/pdfs/zoo.pdf");


        // -------------------chunking
        TextChunkingService chunkingService = new TextChunkingService();
        List<String> cadenzaChunks = chunkingService.chunkWithOverlap(cadenza, 200, 100);
        List<String> greenageChunks = chunkingService.chunkWithOverlap(greenage, 200, 100);
        List<String> zooChunks = chunkingService.chunkWithOverlap(zoo, 200, 100);

        List<String> allChunks = new ArrayList<>();
        allChunks.addAll(cadenzaChunks);
        allChunks.addAll(greenageChunks);
        allChunks.addAll(zooChunks);
        System.out.println("************** All Chunks ********************");
        System.out.println(allChunks);

        //-------------------- embedding and storing in vector db for cadenza
        LLMService llmService = new GeminiLLMService();
        //LLMService llmService = new OpenAILLMService();
        InMemoryVectorStoreService vectorStoreService = new InMemoryVectorStoreService();

        int i = 1;
        for (String chunk: allChunks) {
            System.out.println("Chunk #" + i++ + ": " +chunk);
            List<Double> embedding = llmService.getEmbedding(chunk);
            vectorStoreService.addItem(chunk, embedding);
        }


        ///............... Going to the question/retrieval part now---------------
        RetrievalService retrievalService = new GeminiRetrievalService();
//        RetrievalService retrievalService = new OpenAIRetrievalService();

//        String question = "what is the swimming pool depth in cadenza?";
//        String question = "what is the swimming pool depth in greenage?";
//        String question = "what is address of benerghatta national park and what is the ticket cost?";
//        String question = "what is the size of the Bannerghatta?";
//        String question = "which city is Bannerghatta Zoo located in?";
        String question = "what kind of animals are in the Amazon Rain forest?";;


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
