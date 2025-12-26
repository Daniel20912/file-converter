package com.danieloliveira.file_converter.image.exceptions;

public class ImageCorruptedOrEmptyException extends RuntimeException {
    public ImageCorruptedOrEmptyException(String message) {
        super(message);
    }
}
