package com.danieloliveira.file_converter.document.controller;

import com.danieloliveira.file_converter.document.exceptions.DocumentCorruptedOrEmptyException;
import com.danieloliveira.file_converter.document.exceptions.InvalidDocumentFormatException;
import com.danieloliveira.file_converter.document.model.DocFormat;
import com.danieloliveira.file_converter.document.service.DocumentConverterService;
import com.danieloliveira.file_converter.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/conversions/documents")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Document Conversion", description = "Endpoints for converting office documents to different formats.")
public class DocumentConverterController {

    private final DocumentConverterService service;

    @Operation(summary = "Convert to Standard PDF", description = "Converts DOCX, TXT, XLSX, XLS, PPTX and PPT files to a standard PDF format with embedded fonts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File converted successfully",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "400", description = "Bad Request: File is empty or corrupted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type: Input format not supported",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toPDF(
            @Parameter(description = "The document file to convert", required = true)
            @RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new DocumentCorruptedOrEmptyException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.PDF);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.pdf\"")
                .body(convertedDocument);
    }

    @Operation(summary = "Convert to PDF/A (Archival)", description = "Converts documents to PDF/A-2b format, compliant with ISO 19005 standards for long-term archiving.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF/A file created successfully",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "400", description = "Bad Request: File is empty or corrupted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-pdfa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toPDFA(
            @Parameter(description = "The document file to convert", required = true)
            @RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new DocumentCorruptedOrEmptyException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.PDFA);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.pdf\"")
                .body(convertedDocument);
    }

    @Operation(summary = "Convert to DOCX (Word)", description = "Converts text-based files (TXT) to Microsoft Word format.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File converted successfully to DOCX",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")),
            @ApiResponse(responseCode = "400", description = "Bad Request: File is empty",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type: PDF input is not allowed for this endpoint",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-docx", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toDOCX(
            @Parameter(description = "The document file to convert (PDF is not allowed)", required = true)
            @RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new DocumentCorruptedOrEmptyException("Document file is empty or corrupted");
        }

        if (Objects.equals(document.getContentType(), DocFormat.PDF.getMimeType())) {
            throw new InvalidDocumentFormatException("The provided file cannot be converted to the requested format.");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.DOCX);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.docx\"")
                .body(convertedDocument);
    }

    @Operation(summary = "Extract Text (TXT)", description = "Extracts plain text from documents, including PDFs, DOCX.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Text extracted successfully",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Bad Request: File is empty",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Text extraction failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-txt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toTXT(
            @Parameter(description = "The document file to extract text from", required = true)
            @RequestParam("file") MultipartFile document) throws IOException {

        if (document.isEmpty()) {
            throw new DocumentCorruptedOrEmptyException("Document file is empty or corrupted");
        }

        byte[] convertedDocument = service.documentConverter(document, DocFormat.TXT);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_converted.txt\"")
                .body(convertedDocument);
    }
}
