package com.danieloliveira.file_converter.document.model;

public enum DocumentFormat {
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx" ),
    PDF("application/pdf", "pdf" ),
    TXT("text/plain", "txt");

    private final String mimeType;
    private final String extension;

    DocumentFormat(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
}
