package org.motechproject.nms.mobileacademy.dto;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

//This is just a placeholder for save bookmark with score API.
//actual implementation would be done in sprint 1504
public class BookmarkWithScore implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String callingNumber;

    @JsonProperty
    private String callId;

    @JsonProperty
    private String bookmark;

    @JsonProperty
    private Map<String, String> scoresByChapter;

    @Override
    public String toString() {
        return "BookmarkWithScore{callingNumber=" + callingNumber + ", callId="
                + callId + ", bookmark=" + bookmark + ", scoresByChapter="
                + scoresByChapter + "}";
    }
}
