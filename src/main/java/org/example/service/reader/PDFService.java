package org.example.service.reader;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PDFService {

    public String readPDF(String path) throws IOException {
        //System.out.println(new String(IOUtils.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream(path)))));
        //PDDocument document = PDDocument.load(getClass().getResourceAsStream(path));
        PDDocument document = PDDocument.load(new File(path));
        String text = new PDFTextStripper().getText(document);
        document.close();
        return text;
    }
}
