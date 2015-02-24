package org.motechproject.nms.mobileacademy.commons;

/**
 * Created by nitin on 2/13/15.
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
