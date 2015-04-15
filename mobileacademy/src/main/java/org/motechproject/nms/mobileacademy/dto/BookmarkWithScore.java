package org.motechproject.nms.mobileacademy.dto;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * This class is used for getting the input parameters from saveBookmarkWithScore API
 *
 */
public class BookmarkWithScore implements Serializable {

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

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public Map<String, String> getScoresByChapter() {
        return scoresByChapter;
    }

    public void setScoresByChapter(Map<String, String> scoresByChapter) {
        this.scoresByChapter = scoresByChapter;
    }
}
