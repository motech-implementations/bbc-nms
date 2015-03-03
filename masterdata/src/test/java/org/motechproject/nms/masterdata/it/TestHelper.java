package org.motechproject.nms.masterdata.it;

import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.domain.StateCsv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishek on 3/3/15.
 */
public class TestHelper {

    private static final String OPERATION=null;

    public static StateCsv getStateCsvData(){

        StateCsv stateCsvData = new StateCsv("Add","MotechEventCreateTest","123",null, null);

        return stateCsvData;
    }

    public static MotechEvent createMotechEvent(List<Long> ids,String event) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "");
        return new MotechEvent(event, params);
    }
}
