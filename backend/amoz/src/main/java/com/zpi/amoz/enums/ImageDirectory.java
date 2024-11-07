package com.zpi.amoz.enums;

public enum ImageDirectory {
    USER_IMAGES("user_images"),
    PRODUCT_VARIANT_IMAGES("product_variant_images"),
    COMPANY_IMAGES("company_images");

    private final String directoryName;
    ImageDirectory(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
