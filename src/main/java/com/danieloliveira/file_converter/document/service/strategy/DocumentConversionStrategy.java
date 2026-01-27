package com.danieloliveira.file_converter.document.service.strategy;

import com.danieloliveira.file_converter.document.model.DocFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentConversionStrategy {

    boolean canConvert(String sourceMimeType, DocFormat targetFormat);

    byte[] convert(MultipartFile file, DocFormat targetFormat) throws IOException;
}
