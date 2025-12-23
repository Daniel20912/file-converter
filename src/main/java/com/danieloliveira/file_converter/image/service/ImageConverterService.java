package com.danieloliveira.file_converter.image.service;

import com.danieloliveira.file_converter.image.model.ImageFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class ImageConverterService {

    public byte[] imageConverter(MultipartFile originalFile, ImageFormat targetFormat) throws IOException {

        String incomingMimetype = originalFile.getContentType();

        boolean isSupported = Arrays.stream(ImageFormat.values())
                .anyMatch(imageFormat -> imageFormat.getMimeType().equals(incomingMimetype));

        if (!isSupported) {
            throw new IllegalArgumentException("Invalid image format");
        }

        try (InputStream inputStream = originalFile.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, targetFormat.name().toLowerCase(), baos);

            return baos.toByteArray();
        }
    }
}
