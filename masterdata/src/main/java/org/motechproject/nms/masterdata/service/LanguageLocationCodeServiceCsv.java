package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;

public interface LanguageLocationCodeServiceCsv {

    LanguageLocationCodeCsv getRecord(Long id);

    void delete(LanguageLocationCodeCsv record);

}
