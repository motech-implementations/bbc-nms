package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;

import java.util.List;

public interface LanguageLocationCodeDataService extends MotechDataService<LanguageLocationCode> {


    @Lookup
    LanguageLocationCode findByLocationCode(@LookupField(name = "stateCode") Long stateCode,
                                            @LookupField(name = "districtCode") Long districtCode);

    @Lookup
    List<LanguageLocationCode> findByCircleCode(@LookupField(name = "circleCode") String circleCode);

    @Lookup
    LanguageLocationCode findByCircleCodeAndLangLocCode(@LookupField(name = "circleCode") String circleCode,
                                                        @LookupField(name = "languageLocationCode")
                                                        Integer languageLocationCode);
}
