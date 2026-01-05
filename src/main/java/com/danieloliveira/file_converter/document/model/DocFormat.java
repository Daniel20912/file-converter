package com.danieloliveira.file_converter.document.model;

import lombok.Getter;

@Getter
public enum DocFormat {
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    DOC("application/msword", "doc"),
    PDF("application/pdf", "pdf"),
    PDFA("application/pdf", "pdf"),
    TXT("text/plain", "txt");

    private final String mimeType;
    private final String extension;

    DocFormat(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
}
