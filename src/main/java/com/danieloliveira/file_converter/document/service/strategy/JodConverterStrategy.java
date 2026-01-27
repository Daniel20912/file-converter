package com.danieloliveira.file_converter.document.service.strategy;

import com.danieloliveira.file_converter.document.exceptions.ConversionException;
import com.danieloliveira.file_converter.document.model.DocFormat;
import lombok.RequiredArgsConstructor;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JodConverterStrategy implements DocumentConversionStrategy {

    private final DocumentConverter converter;

    @Override
    public boolean canConvert(String sourceMimeType, DocFormat targetFormat) {
        return true; // fallback
    }

    @Override
    public byte[] convert(MultipartFile file, DocFormat targetFormat) {
        DocumentFormat jodFormat = resolveJodFormat(targetFormat);

        try (InputStream inputStream = file.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            converter.convert(inputStream)
                    .to(baos)
                    .as(jodFormat)
                    .execute();

            return baos.toByteArray();

        } catch (IOException | OfficeException e) {
            throw new ConversionException("JOD Conversion failed: " + e.getMessage());
        }
    }

    private DocumentFormat resolveJodFormat(DocFormat targetFormat) {
        if (targetFormat == DocFormat.PDF) {
            return createPdfFormat(false);
        } else if (targetFormat == DocFormat.PDFA) {
            return createPdfFormat(true);
        } else {
            return DefaultDocumentFormatRegistry.getFormatByExtension(targetFormat.getExtension());
        }
    }

    private DocumentFormat createPdfFormat(boolean isPdfA) {
        Map<String, Object> filterData = new HashMap<>();
        filterData.put("Quality", 100);
        filterData.put("ExportBookmarks", true);

        if (isPdfA) {
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
                .storeProperty(DocumentFamily.SPREADSHEET, "FilterData", filterData)
                .build();
    }
}
