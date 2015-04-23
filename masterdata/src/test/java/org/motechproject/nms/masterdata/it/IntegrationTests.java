package org.motechproject.nms.masterdata.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.masterdata.it.CircleCsvHandlerIT;
import org.motechproject.nms.masterdata.it.DistrictCsvHandlerIT;
import org.motechproject.nms.masterdata.it.HealthBlockCsvHandlerIT;
import org.motechproject.nms.masterdata.it.HealthFacilityCsvHandlerIT;
import org.motechproject.nms.masterdata.it.HealthSubFacilityCsvHandlerIT;
import org.motechproject.nms.masterdata.it.LanguageLocationCodeCsvHandlerIT;
import org.motechproject.nms.masterdata.it.LanguageLocationCodeServiceIT;
import org.motechproject.nms.masterdata.it.OperatorCsvHandlerIT;
import org.motechproject.nms.masterdata.it.StateCsvHandlerIT;
import org.motechproject.nms.masterdata.it.TalukaCsvHandlerIT;
import org.motechproject.nms.masterdata.it.VillageCsvHandlerIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CircleCsvHandlerIT.class, DistrictCsvHandlerIT.class,
        HealthBlockCsvHandlerIT.class, HealthFacilityCsvHandlerIT.class,
        HealthSubFacilityCsvHandlerIT.class,
        LanguageLocationCodeCsvHandlerIT.class,
        LanguageLocationCodeServiceIT.class, OperatorCsvHandlerIT.class,
        StateCsvHandlerIT.class, TalukaCsvHandlerIT.class,
        VillageCsvHandlerIT.class })
public class IntegrationTests {
}
