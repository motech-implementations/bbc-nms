package org.motechproject.nms.mobilekunji.domain;

/**
 * Created by abhishek on 23/2/15
 * This enum specified the possible values Content Type
 */
public enum ContentType {

    PROMPT, CONTENT;

    public static ContentType of(String str) {
        for (ContentType contentType : values()) {
            if (contentType.toString().equals(str)) {
                return contentType;
            }
        }
        return null;
    }
}
