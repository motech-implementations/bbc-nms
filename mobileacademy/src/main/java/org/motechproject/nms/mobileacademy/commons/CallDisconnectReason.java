package org.motechproject.nms.mobileacademy.commons;

/**
 * CallDisconnectReason enumeration specify call disconnect reasons.
 *
 */
public enum CallDisconnectReason {
    NORMAL_DROP(1), VXML_RUNTIME_EXCEPTION(2), CONTENT_NOT_FOUND(3), USAGE_CAP_EXCEEDED(
            4), ERROR_IN_THE_API(5), SYSTEM_ERROR(6);

    private final Integer value;

    private CallDisconnectReason(Integer value) {
        this.value = value;
    }

    /**
     * Get value of particular CallDisconnectReason instance i.e
     * CallDisconnectReason.NORMAL_DROP.getValue() return 1
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * find CallDisconnectReason instance By Value
     * 
     * @param value
     * @return CallDisconnectReason
     */
    public static CallDisconnectReason findByValue(final Integer value) {
        CallDisconnectReason callDisconnectReasonReturn = null;
        for (CallDisconnectReason callDisconnectReason : CallDisconnectReason
                .values()) {
            if (callDisconnectReason.value.equals(value)) {
                callDisconnectReasonReturn = callDisconnectReason;
                break;
            }
        }
        return callDisconnectReasonReturn;
    }
}
