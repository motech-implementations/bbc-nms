package org.motechproject.nms.mobileacademy.service;

/**
 * This interface contains services from Master data module.
 */
public interface MasterDataService {

    /**
     * check circle valid or not
     * 
     * @param circle circle Code
     * @return boolean status - true for valid
     */
    public boolean isCircleValid(String circleCode);

    /**
     * check whether LLC valid in circle or not
     * 
     * @param Circle circle code
     * @param LLC language Location Code
     * @return boolean status -true for valid
     */
    public boolean isLlcValidInCircle(String circleCode, Integer languageLocCode);
}
