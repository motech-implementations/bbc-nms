package org.motechproject.nms.kilkari.ut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.event.handler.ChildMctsCsvHandler;
import org.motechproject.nms.kilkari.event.handler.MotherMctsCsvHandler;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;

public class MotherMctsChildMctsSuccessTest {

    @Mock
    protected MotherMctsCsvService motherMctsCsvService;

    @Mock
    protected ChildMctsCsvService childMctsCsvService;
    
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testValidateMotherSuccessEvent() {

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(50l);
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ChildMctsCsvUT.csv");

        ChildMctsCsvHandler childMctsCsvHandler = new ChildMctsCsvHandler(childMctsCsvService);

        MotechEvent motechEvent = new MotechEvent("ChildMctsCsv.csv_success", parameters);
        childMctsCsvHandler.childMctsCsvSuccess(motechEvent);


    }
    
    @Test
    public void testValidateChildSuccessEvent() {

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(50l);
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ChildMctsCsvUT.csv");

        MotherMctsCsvHandler childMctsCsvHandler = new MotherMctsCsvHandler(motherMctsCsvService);

        MotechEvent motechEvent = new MotechEvent("ChildMctsCsv.csv_success", parameters);
        childMctsCsvHandler.motherMctsCsvSuccess(motechEvent);


    }
}
