package org.motechproject.nms.kilkari.domain;

/**
 * This enum is used for reason for deactivation of subscription.
 */
public enum DeactivationReason {

    CHILD_DEATH,
    MOTHER_DEATH,
    ABORTION,
    STILL_BIRTH,
    MSISDN_IN_DND,
    INVALID_MSISDN,
    USER_DEACTIVATED,
    PACK_CHANGED,
    PACK_SCHEDULE_CHANGED,
    NONE;

}
