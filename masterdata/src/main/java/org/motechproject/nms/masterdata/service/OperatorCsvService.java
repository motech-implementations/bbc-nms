package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.OperatorCsv;

public interface OperatorCsvService {

    OperatorCsv getRecord(Long id);

    void delete(OperatorCsv record);

}
