package org.motechproject.nms.mobileacademy.commons;

/**
 * Enumeration for course content type.
 */
public enum ContentType {
    CONTENT("Content"), PROMPT("Prompt");

    private final String name;

    private ContentType(String name) {
        this.name = name;
    }

    /**
     * Get contentType name i.e ContentType.CONTENT.getName() return Content
     * string
     * 
     * @return String name of content type i.e Content
     */
    public String getName() {
        return this.name;
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
            if (contentType.name.equalsIgnoreCase(contentTypeName)) {
                contentTypeReturn = contentType;
                break;
            }
        }
        return contentTypeReturn;
    }
}
