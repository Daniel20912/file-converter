package com.danieloliveira.file_converter.image.controller;

import com.danieloliveira.file_converter.image.model.ImageFormat;
import com.danieloliveira.file_converter.image.service.ImageConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/conversions/images")
@RequiredArgsConstructor
public class ConverterController {

    private final ImageConverterService service;

    @PostMapping("/to-png")
    public ResponseEntity<byte[]> toPNG(@RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.PNG);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.png\"")
                .body(convertedImage);
    }

    @PostMapping("/to-jpeg")
    public ResponseEntity<byte[]> toJPG(@RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.JPEG);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.jpeg\"")
                .body(convertedImage);
    }

    @PostMapping("/to-webp")
    public ResponseEntity<byte[]> toWEBP(@RequestParam("file") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        byte[] convertedImage = service.imageConverter(image, ImageFormat.WEBP);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ImageFormat.WEBP.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image_converted.webp\"")
                .body(convertedImage);
    }
}
