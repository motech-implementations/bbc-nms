package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.*;

public interface LocationService {

    boolean validateLocation(Long stateId, Long districtId);
}
