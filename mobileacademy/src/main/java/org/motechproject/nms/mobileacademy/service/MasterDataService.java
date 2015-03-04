package org.motechproject.nms.mobileacademy.service;

/**
 * This is a place holder for Master data service. Will be removed later.
 */
public interface MasterDataService {

    /**
     * check circle vaild or not
     * 
     * @param circle
     * @return boolean
     */
    public boolean isCircleValid(String circle);

    /**
     * check LLC valid in circle or not
     * 
     * @param Circle
     * @param LLC
     * @return boolean
     */
    public boolean isLlcValidInCircle(String Circle, int LLC);
}
