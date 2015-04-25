package org.motechproject.nms.kilkari.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.event.handler.CsvMctsChildHandler;
import org.motechproject.nms.kilkari.event.handler.CsvMctsMotherHandler;
import org.motechproject.nms.kilkari.service.CsvMctsChildService;
import org.motechproject.nms.kilkari.service.CsvMctsMotherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MctsMotherMctsChildSuccessTest {

    @Mock
    protected CsvMctsMotherService csvMctsMotherService;

    @Mock
    protected CsvMctsChildService csvMctsChildService;
    
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
        parameters.put("csv-import.filename", "CsvMctsChildUT.csv");

        CsvMctsChildHandler csvMctsChildHandler = new CsvMctsChildHandler(csvMctsChildService);

        MotechEvent motechEvent = new MotechEvent("CsvMctsChild.csv_success", parameters);
        csvMctsChildHandler.csvMctsChildSuccess(motechEvent);


    }
    
    @Test
    public void testValidateChildSuccessEvent() {

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(50l);
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "CsvMctsChildUT.csv");

        CsvMctsMotherHandler csvMctsChildHandler = new CsvMctsMotherHandler(csvMctsMotherService);

        MotechEvent motechEvent = new MotechEvent("CsvMctsChild.csv_success", parameters);
        csvMctsChildHandler.csvMctsMotherSuccess(motechEvent);


    }
}
