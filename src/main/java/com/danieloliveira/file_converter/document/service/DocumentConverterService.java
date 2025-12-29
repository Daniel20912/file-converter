package com.danieloliveira.file_converter.document.service;

import com.danieloliveira.file_converter.document.model.DocumentFormat;
import lombok.RequiredArgsConstructor;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

        var jodFormat = DefaultDocumentFormatRegistry.getFormatByExtension(targetFormat.getExtension());

        try (InputStream inputStream = originalFile.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            assert jodFormat != null;
            converter.convert(inputStream)
                    .to(baos)
                    .as(jodFormat)
                    .execute();

            return baos.toByteArray();

        } catch (OfficeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
