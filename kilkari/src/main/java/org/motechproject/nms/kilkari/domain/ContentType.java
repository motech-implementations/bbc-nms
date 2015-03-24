package org.motechproject.nms.kilkari.domain;

/**
 * This enum represents the type of Content that IVR shall play
 * Prompt or Content 
 */
public enum ContentType {

    PROMPT("Prompt"), CONTENT("Content");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    /**
     * find ContentType object By contentName
     *
     * @param contentTypeName name of the content type i.e Content, Prompt
     * @return ContentType object return and it can be null also
     */
    public static ContentType findByName(final String contentTypeName) {
        ContentType contentTypeReturn = null;
        for (ContentType contentType : ContentType.values()) {
            if (contentType.value.equalsIgnoreCase(contentTypeName)) {
                contentTypeReturn = contentType;
                break;
            }
        }
        return contentTypeReturn;
    }
}
