package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.util.helper.DataValidationException;

/**
 * The purpose of this class is to provide methods to save call details the service level SaveCallDetails.
 */
public interface SaveCallDetailsService {

    public void saveCallDetails(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException;
}
