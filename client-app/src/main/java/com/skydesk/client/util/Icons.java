package com.skydesk.client.util;

public class Icons {

    public enum IconType {
        ICON_PDF, ICON_IMAGE, ICON_UPLOAD, DEFAULT
    }

    public static String getPath(IconType iconType) {
        return switch (iconType) {
            case ICON_IMAGE -> "/icon/image.png";
            case ICON_PDF -> "/icon/pdf.png";
            case ICON_UPLOAD -> "/icon/upload.png";
            case DEFAULT -> "/icon/text.png";
        };
    }
}
