package org.motechproject.nms.mobileacademy.commons;

/**
 * Enumeration for course content type.
 */
public enum ContentType {
    Content, Prompt;

    public static ContentType getFor(String type) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.equals(type))
                return contentType;
        }
        return null;
    }
}
