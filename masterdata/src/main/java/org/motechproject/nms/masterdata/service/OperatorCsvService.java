package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.OperatorCsv;

public interface OperatorCsvService {

    OperatorCsv findById(Long id);

    void delete(OperatorCsv record);

    void deleteAll();
}
