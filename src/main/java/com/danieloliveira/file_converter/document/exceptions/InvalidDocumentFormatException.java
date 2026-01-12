package com.danieloliveira.file_converter.document.exceptions;

public class InvalidDocumentFormatException extends RuntimeException {
    public InvalidDocumentFormatException(String message) {
        super(message);
    }
}
