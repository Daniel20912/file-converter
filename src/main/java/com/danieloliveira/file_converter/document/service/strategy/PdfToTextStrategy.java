package com.danieloliveira.file_converter.document.service.strategy;

import com.danieloliveira.file_converter.document.exceptions.TextExtractionException;
import com.danieloliveira.file_converter.document.model.DocFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(1)
public class PdfToTextStrategy implements DocumentConversionStrategy {

    @Override
    public boolean canConvert(String sourceMimeType, DocFormat targetFormat) {
        return (sourceMimeType.equals(DocFormat.PDF.getMimeType()) ||
                sourceMimeType.equals(DocFormat.PDFA.getMimeType()))
                && targetFormat == DocFormat.TXT;
    }

    @Override
    public byte[] convert(MultipartFile file, DocFormat targetFormat) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());

            String text = stripper.getText(document);
            return text.getBytes(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new TextExtractionException("Error extracting text from PDF: " + e.getMessage());
        }
    }
}
