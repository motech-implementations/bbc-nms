package org.motechproject.nms.mobileacademy.commons;

/**
 * CappingType enumeration specify various capping types for mobile academy
 * service.
 *
 */
public enum CappingType {

    NO_CAPPING(0), NATIONAL_CAPPING(1), STATE_CAPPING(2);

    private final Integer value;

    private CappingType(Integer value) {
        this.value = value;
    }

    /**
     * get value of particular capping type instance i.e
     * CappingType.NO_CAPPING.getValue() return 0
     */
    public Integer getValue() {
        return this.value;
    }
}
