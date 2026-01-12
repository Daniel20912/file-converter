package com.danieloliveira.file_converter.exceptions.controller;

import com.danieloliveira.file_converter.document.exceptions.ConversionException;
import com.danieloliveira.file_converter.document.exceptions.DocumentCorruptedOrEmptyException;
import com.danieloliveira.file_converter.document.exceptions.InvalidDocumentFormatException;
import com.danieloliveira.file_converter.document.exceptions.TextExtractionException;
import com.danieloliveira.file_converter.exceptions.ErrorMessage;
import com.danieloliveira.file_converter.image.exceptions.ImageCorruptedOrEmptyException;
import com.danieloliveira.file_converter.image.exceptions.InvalidImageFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IMAGE ERRORS
    @ExceptionHandler(ImageCorruptedOrEmptyException.class)
    public ResponseEntity<ErrorMessage> handleImageCorruptedOrEmptyException(ImageCorruptedOrEmptyException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    public ResponseEntity<ErrorMessage> handleInvalidImageFormatException(InvalidImageFormatException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage()));
    }

    // DOCUMENT ERRORS
    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<ErrorMessage> handleConversionException(ConversionException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(DocumentCorruptedOrEmptyException.class)
    public ResponseEntity<ErrorMessage> handleDocumentCorruptedOrEmptyException(DocumentCorruptedOrEmptyException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(InvalidDocumentFormatException.class)
    public ResponseEntity<ErrorMessage> handleInvalidDocumentFormatException(InvalidDocumentFormatException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage()));
    }

    @ExceptionHandler(TextExtractionException.class)
    public ResponseEntity<ErrorMessage> handleTextExtractionException(TextExtractionException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
