package org.example.service.reader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFService {

    public String readPDF(String path) throws IOException {
        PDDocument document = PDDocument.load(new File(path));
        String text = new PDFTextStripper().getText(document);
        document.close();
        return text;
    }
}
