package org.motechproject.nms.masterdata.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CircleCsvHandlerIT.class, LanguageLocationCodeCsvHandlerIT.class,
        LanguageLocationCodeServiceIT.class, OperatorCsvHandlerIT.class})
public class MasterdataIntegrationTests {
}