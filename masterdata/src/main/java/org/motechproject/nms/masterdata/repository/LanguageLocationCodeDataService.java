package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;

import java.util.List;

/**
 * This interface is used to operate on language locatio code using Motech Data service
 */
public interface LanguageLocationCodeDataService extends MotechDataService<LanguageLocationCode> {


    /**
     * find the language location code by its location code
     *
     * @param stateCode
     * @param districtCode
     * @return LanguageLocationCode
     */
    @Lookup
    LanguageLocationCode findByLocationCode(@LookupField(name = "stateCode") Long stateCode,
                                            @LookupField(name = "districtCode") Long districtCode);

    /**
     * Finds language location code Circle wise
     *
     * @param circleCode
     * @return List&lt;LanguageLocatioCode&gt;
     */
    @Lookup
    List<LanguageLocationCode> findByCircleCode(@LookupField(name = "circleCode") String circleCode);

    /**
     * Finds the language location code by its Circle code and language location code
     *
     * @param circleCode
     * @param languageLocationCode
     * @return LanguageLocationCode
     */
    @Lookup
    List<LanguageLocationCode> findByCircleCodeAndLangLocCode(@LookupField(name = "circleCode") String circleCode,
                                                              @LookupField(name = "languageLocationCode")
                                                              String languageLocationCode);

    @Lookup
    List<LanguageLocationCode> findLLCByCode(@LookupField(name = "languageLocationCode") String code);
}
