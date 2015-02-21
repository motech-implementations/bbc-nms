package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;

public interface CircleCsvService {

    CircleCsv findById(Long id);

    void delete(CircleCsv record);

    void deleteAll();

    Circle getCircleByCode(String circleCode);
}
