package org.motechproject.nms.masterdata.repository;

import org.apache.commons.codec.language.bm.Lang;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;

import java.util.List;

public interface LanguageLocationCodeDataService extends MotechDataService<LanguageLocationCode> {


    @Lookup
    LanguageLocationCode findLanguageLocationCodeByLocation(@LookupField(name="stateCode") Integer stateCode,
                                                  @LookupField(name="districtCode") Integer districtCode);

    @Lookup
    List<LanguageLocationCode> findLanguageLocationCodeByCircle(@LookupField(name="stateCode") String circleCode);
}
