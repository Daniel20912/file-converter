package com.danieloliveira.file_converter.image.controller;

import com.danieloliveira.file_converter.exceptions.ErrorMessage;
import com.danieloliveira.file_converter.image.exceptions.ImageCorruptedOrEmptyException;
import com.danieloliveira.file_converter.image.model.ImageFormat;
import com.danieloliveira.file_converter.image.service.ImageConverterService;
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

@RestController
@RequestMapping("/api/v1/conversions/images")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Image Conversion", description = "Endpoints for converting image files (JPEG, PNG, WEBP, etc.) to different formats.")
public class ImageConverterController {

    private final ImageConverterService service;

    @Operation(summary = "Convert to PNG", description = "Converts an input image file to PNG format. Supports transparency.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image converted successfully",
                    content = @Content(mediaType = "image/png")),
            @ApiResponse(responseCode = "400", description = "Bad Request: Image is empty or corrupted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type: Input file is not a valid image",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-png", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toPNG(
            @Parameter(description = "The image file to convert", required = true)
            @RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new ImageCorruptedOrEmptyException("Image file is empty or corrupted");
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.PNG);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.png\"")
                .body(convertedImage);
    }


    @Operation(summary = "Convert to JPEG", description = "Converts an input image file to JPEG format. Automatically replaces transparency with a white background.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image converted successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "400", description = "Bad Request: Image is empty or corrupted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type: Input file is not a valid image",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-jpeg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toJPG(
            @Parameter(description = "The image file to convert", required = true)
            @RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new ImageCorruptedOrEmptyException("Image file is empty or corrupted");
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.JPEG);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.jpeg\"")
                .body(convertedImage);
    }


    @Operation(summary = "Convert to WebP", description = "Converts an input image file to WebP format for optimized web usage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image converted successfully",
                    content = @Content(mediaType = "image/webp")),
            @ApiResponse(responseCode = "400", description = "Bad Request: Image is empty or corrupted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type: Input file is not a valid image",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Conversion failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/to-webp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> toWEBP(
            @Parameter(description = "The image file to convert", required = true)
            @RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new ImageCorruptedOrEmptyException("Image file is empty or corrupted");
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.WEBP);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ImageFormat.WEBP.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.webp\"")
                .body(convertedImage);
    }
}
