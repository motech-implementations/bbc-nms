package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;

public interface LanguageLocationCodeServiceCsv {

    LanguageLocationCodeCsv findById(Long id);

    void delete(LanguageLocationCodeCsv record);

    void deleteAll();
}
