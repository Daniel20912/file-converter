package com.danieloliveira.file_converter.document.controller;

import com.danieloliveira.file_converter.document.model.DocFormat;
import com.danieloliveira.file_converter.document.service.DocumentConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/conversions/documents")
@RequiredArgsConstructor
public class DocumentConverterController {

    private final DocumentConverterService service;

    @PostMapping("/to-pdf")
    public ResponseEntity<byte[]> toPDF(@RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new IllegalArgumentException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.PDF);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.pdf\"")
                .body(convertedDocument);
    }

    @PostMapping("/to-pdfa")
    public ResponseEntity<byte[]> toPDFA(@RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new IllegalArgumentException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.PDFA);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.pdf\"")
                .body(convertedDocument);
    }

    @PostMapping("/to-docx")
    public ResponseEntity<byte[]> toDOCX(@RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new IllegalArgumentException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.DOCX);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.docx\"")
                .body(convertedDocument);
    }

    @PostMapping("/to-txt")
    public ResponseEntity<byte[]> toTXT(@RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new IllegalArgumentException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.TXT);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.txt\"")
                .body(convertedDocument);
    }
}
