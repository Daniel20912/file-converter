package com.danieloliveira.file_converter.document.service;

import com.danieloliveira.file_converter.document.exceptions.ConversionException;
import com.danieloliveira.file_converter.document.exceptions.InvalidDocumentFormatException;
import com.danieloliveira.file_converter.document.model.DocFormat;
import com.danieloliveira.file_converter.document.service.strategy.DocumentConversionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentConverterService {

    private final List<DocumentConversionStrategy> strategies;

    public byte[] documentConverter(MultipartFile originalFile, DocFormat targetFormat) throws IOException {
        String incomingMimeType = originalFile.getContentType();
        validateInputFormat(incomingMimeType);

        DocumentConversionStrategy strategy = strategies.stream()
                .filter(s -> s.canConvert(incomingMimeType, targetFormat))
                .findFirst()
                .orElseThrow(() -> new ConversionException("No conversion strategy found for this format pair."));

        return strategy.convert(originalFile, targetFormat);
    }


    private void validateInputFormat(String mimeType) {
        if (mimeType == null) throw new InvalidDocumentFormatException("MimeType is null");

        MediaType mediaType = MediaType.parseMediaType(mimeType);

        boolean isSupported = Arrays.stream(DocFormat.values())
                .map(fmt -> MediaType.parseMediaType(fmt.getMimeType()))
                .anyMatch(supported -> supported.includes(mediaType));

        if (!isSupported) {
            throw new InvalidDocumentFormatException("Format no supported: " + mimeType);
        }
    }
}
