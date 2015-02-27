package org.motechproject.nms.mobilekunji.event.handler.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.Long;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;


/**
 * Unit tests for mTraining service
 */
public class ContentUploadCsvHandlerUnitTest {

    @Mock
    private ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    @Mock
    private ContentUploadRecordDataService contentUploadRecordDataService;

    @Mock
    private BulkUploadErrLogService bulkUploadErrLogService;

    ContentUploadCsv uploadCsv;
    Long dbIndex= Long.parseLong("1");

    @Before
    public void setup() {
        initMocks(this);

        uploadCsv = new ContentUploadCsv(dbIndex, "ADD", "1", "22",
                "33", "malaria", "CONTENT", "malaria.war",
                "4", "30");
        uploadCsv.setId(dbIndex);
        contentUploadCsvRecordDataService.create(uploadCsv);


    }

    @Test
    public void createCourse() {

    }


}
