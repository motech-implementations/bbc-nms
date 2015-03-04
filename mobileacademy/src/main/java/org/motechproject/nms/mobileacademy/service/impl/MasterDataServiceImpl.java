package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.service.MasterDataService;
import org.springframework.stereotype.Service;

/**
 * This is dummy implementation of Master data service. Will be removed later.
 *
 */
@Service("MasterDataService")
public class MasterDataServiceImpl implements MasterDataService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.MasterDataService#isCircleValid
     * (java.lang.String)
     */
    @Override
    public boolean isCircleValid(String circle) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.MasterDataService#
     * isLLCValidInCircle(java.lang.String, int)
     */
    @Override
    public boolean isLlcValidInCircle(String Circle, int LLC) {
        // TODO Auto-generated method stub
        return true;
    }

}
