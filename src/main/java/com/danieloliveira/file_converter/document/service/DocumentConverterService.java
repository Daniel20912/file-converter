package com.danieloliveira.file_converter.document.service;

import com.danieloliveira.file_converter.document.model.DocFormat;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentConverterService {

    private final DocumentConverter converter;

    public byte[] documentConverter(MultipartFile originalFile, DocFormat targetFormat) throws IOException {

        String incomingMimeType = originalFile.getContentType();

        assert incomingMimeType != null;
        MediaType incomingMediaType = MediaType.parseMediaType(incomingMimeType);

        boolean isSupported = Arrays.stream(DocFormat.values())
                .map(fmt -> MediaType.parseMediaType(fmt.getMimeType()))
                .anyMatch(supportedMediaType -> supportedMediaType.includes(incomingMediaType));

        if (!isSupported) {
            throw new IllegalArgumentException("Document format not supported: " + incomingMimeType);
        }

        if (originalFile.getContentType().equals(DocFormat.PDF.getMimeType()) && targetFormat == DocFormat.TXT) {
            return extractTextFromPDF(originalFile);
        }

        DocumentFormat jodTargetFormat;

        if (targetFormat == DocFormat.PDF) {
            jodTargetFormat = createPDF(false);
        } else if (targetFormat == DocFormat.PDFA) {
            jodTargetFormat = createPDF(true);
        } else {
            jodTargetFormat = DefaultDocumentFormatRegistry.getFormatByExtension(targetFormat.getExtension());
        }

        try (InputStream inputStream = originalFile.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            assert jodTargetFormat != null;
            converter.convert(inputStream)
                    .to(baos)
                    .as(jodTargetFormat)
                    .execute();

            return baos.toByteArray();

        } catch (OfficeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private byte[] extractTextFromPDF(MultipartFile pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {

            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());

            String text = stripper.getText(document);

            return text.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DocumentFormat createPDF(boolean isPDFA) {
        Map<String, Object> filterData = new HashMap<>();

        filterData.put("Quality", 90);
        filterData.put("ExportBookmarks", true);
        filterData.put("ExportNotes", false);

        if  (isPDFA) {
            filterData.put("SelectPdfVersion", 2);
            filterData.put("UseTaggedPDF", true);
            filterData.put("ExportFormFields", true);
        } else {
            filterData.put("SelectPdfVersion", 0);
            filterData.put("IsEmbedAllFonts", true);
            filterData.put("IsSubsetEmbed", true);
        }

        return DocumentFormat.builder()
                .from(DefaultDocumentFormatRegistry.PDF)
                .storeProperty(DocumentFamily.TEXT, "FilterData", filterData)
                .build();
    }
}
