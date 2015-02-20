package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

import java.util.List;


/**
 * Created by nitin on 2/9/15.
 */
public interface CourseRawContentDataService extends MotechDataService<CourseRawContent>{

    /**
     * find course raw content records by language Location Code and operation
     *
     * @param languageLocationCode language Location Code identifier.
     * @param operation operation i.e ADD/MOD/DEL
     * @return list of CourseRawContent records.
     */
    @Lookup
    List<CourseRawContent> findContentByLlcAndOperation(
            @LookupField(name = "languageLocationCode") String languageLocationCode,
            @LookupField(name = "operation") String operation);

}
