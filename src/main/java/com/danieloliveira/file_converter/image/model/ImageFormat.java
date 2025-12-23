package com.danieloliveira.file_converter.image.model;

import lombok.Getter;

@Getter
public enum ImageFormat {
    PNG("image/png"),
    JPEG("image/jpeg");

    private final String mimeType;

    ImageFormat(String mimeType) {
        this.mimeType = mimeType;
    }

}
