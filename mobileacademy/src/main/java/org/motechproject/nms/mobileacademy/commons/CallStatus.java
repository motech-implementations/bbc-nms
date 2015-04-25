package org.motechproject.nms.mobileacademy.commons;

/**
 * CallStatus enumeration specify call status values.
 *
 */
public enum CallStatus {
    SUCCESS(1), FAILED(2), REJECTED(3);

    private final Integer value;

    private CallStatus(Integer value) {
        this.value = value;
    }

    /**
     * Get value of particular CallStatus instance i.e
     * CallStatus.SUCCESS.getValue() return 1
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * find CallStatus instance By Value
     * 
     * @param value
     * @return CallStatus
     */
    public static CallStatus findByValue(final Integer value) {
        CallStatus callStatusReturn = null;
        for (CallStatus callStatus : CallStatus.values()) {
            if (callStatus.value.equals(value)) {
                callStatusReturn = callStatus;
                break;
            }
        }
        return callStatusReturn;
    }
}
