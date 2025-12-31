package com.danieloliveira.file_converter.document.service;

import com.danieloliveira.file_converter.document.model.DocumentFormat;
import lombok.RequiredArgsConstructor;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.office.OfficeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentConverterService {

    private final DocumentConverter converter;

    public byte[] documentConverter(MultipartFile originalFile, DocumentFormat targetFormat) throws IOException {

        String incomingMimeType = originalFile.getContentType();

        assert incomingMimeType != null;
        MediaType incomingMediaType = MediaType.parseMediaType(incomingMimeType);

        boolean isSupported = Arrays.stream(DocumentFormat.values())
                .map(fmt -> MediaType.parseMediaType(fmt.getMimeType()))
                .anyMatch(supportedMediaType -> supportedMediaType.includes(incomingMediaType));

        if (!isSupported) {
            throw new IllegalArgumentException("Document format not supported: " + incomingMimeType);
        }

        org.jodconverter.core.document.DocumentFormat jodTargetFormat;

        if (targetFormat == DocumentFormat.PDF) {
            jodTargetFormat = createPDF_A1b();
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

    private org.jodconverter.core.document.DocumentFormat createPDF_A1b() {
        Map<String, Object> filterData = new HashMap<>();

        filterData.put("SelectPdfVersion", 2);
        filterData.put("Quality", 90);
        filterData.put("ExportBookmarks", true);
        filterData.put("ExportNotes", false);

        return org.jodconverter.core.document.DocumentFormat.builder()
                .from(DefaultDocumentFormatRegistry.PDF)
                .storeProperty(DocumentFamily.TEXT, "FilterData", filterData)
                .build();
    }
}
