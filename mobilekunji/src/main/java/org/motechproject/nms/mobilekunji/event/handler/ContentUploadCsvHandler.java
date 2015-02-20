package org.motechproject.nms.mobilekunji.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class ContentUploadCsvHandler {


    @Autowired
    private ContentUploadCsvRecordDataService contentUploadCsv;

    @Autowired
    private ContentUploadRecordDataService contentUpload;


    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);


    @MotechListener(subjects = {"mds.crud.mobilekunji.ContentUploadCsv.csv-import.success" })
    public void mobileKunjiContentUploadCsvSuccess(MotechEvent event) {

        List<ContentUploadCsv> mobileKunjiContentUploadCsvList = contentUploadCsv.retrieveAll();

        for (Iterator<ContentUploadCsv> itr = mobileKunjiContentUploadCsvList.iterator(); itr.hasNext();) {

            ContentUploadCsv csvRecord = itr.next();

            if (true == csvRecord.validateParameters()) {

                int contentId = Integer.parseInt(csvRecord.getContentId());
                ContentUpload kunjiData = contentUpload.findRecordByContentId(contentId);

                if (null != kunjiData) {


                } else {


                }

            }


        }
    }

    @MotechListener(subjects = {"mds.crud.mobilekunji.ContentUploadCsv.csv-import.failed" })
    public void mobileKunjiContentUploadCsvFailure(MotechEvent event) {

        contentUploadCsv.deleteAll();

        logger.info("Upload data successfully deleted");
    }




}
