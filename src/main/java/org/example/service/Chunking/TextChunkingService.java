package org.example.service.Chunking;

import java.util.ArrayList;
import java.util.List;

public class TextChunkingService {

    public List<String> chunkText(String text, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize must be greater than 0");
        }

        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += chunkSize) {
            chunks.add(text.substring(i, Math.min(text.length(), i + chunkSize)));
        }
        return chunks;
    }

    public List<String> chunkWithOverlap(String text, int chunkSize, int overlapSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize must be greater than 0");
        }
        if (overlapSize >= chunkSize) {
            throw new IllegalArgumentException("overlapSize must be smaller than chunkSize");
        }

        List<String> chunks = new ArrayList<>();
        int step = chunkSize - overlapSize;

        for (int i = 0; i < text.length(); i += step) {
            chunks.add(text.substring(i, Math.min(text.length(), i + chunkSize)));
        }
        return chunks;
    }
}
