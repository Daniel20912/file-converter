package com.danieloliveira.file_converter.image.service;

import com.danieloliveira.file_converter.image.model.ImageFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageConverterService {

    public byte[] imageConverter(MultipartFile originalFile, ImageFormat imageFormat) throws IOException {

        try (InputStream inputStream = originalFile.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            if (imageFormat == null) throw new IOException("Invalid image format");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, String.valueOf(imageFormat), baos);

            return baos.toByteArray();
        }
    }
}
