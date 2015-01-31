package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MasterDataCsvUploadHandler {

    private static Logger logger = LoggerFactory.getLogger(MasterDataCsvUploadHandler.class);


    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.success")
    public void contentUploadKKCsvSuccess(MotechEvent motechEvent) {

        System.out.println("import successfull");

    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void contentUploadKKCsvFailure(MotechEvent motechEvent) {

        System.out.println("import failed");

    }
}
