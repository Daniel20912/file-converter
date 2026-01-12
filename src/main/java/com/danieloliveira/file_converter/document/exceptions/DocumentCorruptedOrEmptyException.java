package com.danieloliveira.file_converter.document.exceptions;

public class DocumentCorruptedOrEmptyException extends RuntimeException {
    public DocumentCorruptedOrEmptyException(String message) {
        super(message);
    }
}
