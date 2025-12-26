package com.danieloliveira.file_converter.image.model;

import lombok.Getter;

@Getter
public enum ImageFormat {
    PNG("image/png", true),
    JPEG("image/jpeg", false),
    WEBP("image/webp", true);

    private final String mimeType;
    private final boolean supportsTransparency;

    ImageFormat(String mimeType, boolean supportsTransparency) {
        this.mimeType = mimeType;
        this.supportsTransparency = supportsTransparency;
    }

}
