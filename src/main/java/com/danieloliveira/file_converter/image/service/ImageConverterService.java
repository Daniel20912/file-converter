package com.danieloliveira.file_converter.image.service;

import com.danieloliveira.file_converter.image.exceptions.InvalidImageFormatException;
import com.danieloliveira.file_converter.image.model.ImageFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
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
            throw new InvalidImageFormatException("Invalid image format");
        }

        try (InputStream inputStream = originalFile.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            if (!targetFormat.isSupportsTransparency()) {
                image = handleTransparency(image);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, targetFormat.name().toLowerCase(), baos);

            return baos.toByteArray();
        }
    }

    private BufferedImage handleTransparency(BufferedImage originalImage) {
        if (!originalImage.getColorModel().hasAlpha()) {
            return originalImage;
        }

        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = newImage.createGraphics();

        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());

            g.drawImage(originalImage, 0, 0, null);
        } finally {
            g.dispose();
        }

        originalImage.flush();

        return newImage;
    }
}
